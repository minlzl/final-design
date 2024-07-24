package com.example.finalend.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.finalend.entity.MyOrder;
import com.example.finalend.entity.Voucher;
import com.example.finalend.mapper.OrderMapper;
import com.example.finalend.mapper.VoucherMapper;
import com.example.finalend.service.IVoucherService;
import com.example.finalend.utils.SnowIdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class VoucherService extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    VoucherMapper voucherMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    SnowIdUtil snowIdUtil;

    @Resource
    RedissonClient redissonClient;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    ObjectMapper objectMapper;

    private static final String SECKILL_STOCK_KEY = "seckill:stock:";
    private static final String SECKILL_ORDER_KEY = "seckill:order:";

    private static final DefaultRedisScript<Long> SCRIPT;

    private static final ExecutorService SECKILL_ORDER = new ThreadPoolExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1));

    @PostConstruct
    private void init() {
        SECKILL_ORDER.submit(new VoucherOrderHandler());
    }

    private BlockingQueue<MyOrder> myOrderTask = new ArrayBlockingQueue<>(1024 * 1024);
    private class VoucherOrderHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    MyOrder myOrder = myOrderTask.take();
                    handleVoucherOrder(myOrder);
                }catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    static {
        SCRIPT = new DefaultRedisScript<>();
        SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SCRIPT.setResultType(Long.class);
    }

//    IVoucherService proxy;

    @Override
    public Boolean addVoucher(String name, Integer stock) {
        Voucher voucher = new Voucher();
        voucher.setName(name);
        voucher.setStock(stock);
        boolean save = save(voucher);
        redisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock());
        return save;
    }

    @Override
    public Boolean seckillVoucher(Long voucherId, Long userId){
        long orderId = snowIdUtil.nextId();
        String stockKey = SECKILL_STOCK_KEY + voucherId;
        String orderKey = SECKILL_ORDER_KEY + voucherId;
        List<String> keys = new ArrayList<>(Arrays.asList(stockKey, orderKey));
        Long res = redisTemplate.execute(
                SCRIPT,
                keys,
                voucherId,
                userId,
                orderId
        );
        assert res != null;
        int r = res.intValue();
        if (r != 0) {
            if (r == 1) {
                log.info("库存不足");
            } else {
                log.info("不能重复下单");
            }
        }
        MyOrder myOrder = new MyOrder();
        myOrder.setCreateTime(LocalDateTime.now());
        myOrder.setUserId(userId);
        myOrder.setVoucherId(voucherId);
//        myOrderTask.add(myOrder);
        rabbitTemplate.convertAndSend("order.exchange", "order", JSONUtil.toJsonStr(myOrder));
        return Boolean.TRUE;
    }

    private void handleVoucherOrder(MyOrder myOrder) {
        Long userId = myOrder.getUserId();
//        RLock lock = redissonClient.getLock("lock:order:" + userId);
//        boolean isLock = lock.tryLock();
//        if (!isLock) {
//            log.error("不允许重复下单");
//            return;
//        }
//        try {
//            createVoucherOrder(myOrder);
//        } finally {
//            lock.unlock();
//        }
        Boolean member = redisTemplate.opsForSet().isMember("seckill:order:" + myOrder.getVoucherId(), userId);
        if (Boolean.FALSE.equals(member)) {
            createVoucherOrder(myOrder);
        }
    }

    @Transactional
    public void createVoucherOrder(MyOrder myOrder) {
        System.out.println(myOrder);
        Long userId = myOrder.getUserId();
        Long count = orderMapper.selectCount(new QueryWrapper<MyOrder>().eq("user_id", userId).eq("voucher_id", myOrder.getVoucherId()));
        if (count > 0) {
            log.error("用户已经购买过一次");
            return;
        }
        boolean success = update().setSql("stock = stock - 1")
                .eq("id", myOrder.getVoucherId())
                .gt("stock", 0)
                .update();
        if (!success) {
            log.error("库存不足");
            return;
        }
        orderMapper.insert(myOrder);
    }
}
