package com.example.finalend.consumer;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.finalend.constant.RabbitmqConstant;
import com.example.finalend.entity.MyOrder;
import com.example.finalend.mapper.OrderMapper;
import com.example.finalend.service.IVoucherService;
import com.example.finalend.service.impl.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class OrderConsumer {


    @Resource
    IVoucherService iVoucherService;

    @RabbitHandler
    @RabbitListener(queues = RabbitmqConstant.SECKILL_ORDER)
    public void Consumer(String message) {
        MyOrder myOrder = JSONUtil.toBean(message, MyOrder.class);
        iVoucherService.createVoucherOrder(myOrder);
    }
}
