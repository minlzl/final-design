package com.example.finalend;

import com.example.finalend.entity.Talk;
import com.example.finalend.entity.User;
import com.example.finalend.mapper.TalkMapper;
import com.example.finalend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FinalEndApplicationTests {

    @Resource
    UserMapper userMapper;

//    @Test
//    void contextLoads() {
//        User user = new User();
//        user.setId(1l);
////        user.setAvatar("cdscs");
//        userMapper.updateById(user);
//        System.out.println(user);
//    }

}
