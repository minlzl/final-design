package com.example.finalend;

import com.example.finalend.entity.Talk;
import com.example.finalend.entity.User;
import com.example.finalend.mapper.TalkMapper;
import com.example.finalend.mapper.UserMapper;
import com.example.finalend.service.IVoucherService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FinalEndApplicationTests {

    @Resource
    IVoucherService iVoucherService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
        Boolean first = iVoucherService.addVoucher("first", 100);
        System.out.println(false);
    }

    @Test
    void contextLoads1() throws Exception{
        ScanOptions build = ScanOptions.scanOptions().count(2).match("a*").build();
        Cursor<String> scan = redisTemplate.scan(build);
        List<String> ans = new ArrayList<>();
        while (scan.hasNext()) {
            ans.add(scan.next());
        }
        redisTemplate.delete(ans);
    }

}
