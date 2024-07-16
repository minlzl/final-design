package com.example.finalend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.annotation.RedisLimit;
import com.example.finalend.async.impl.AsyncServiceImpl;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.concurrent.Future;

@ApiRestController
@SaCheckLogin
public class TestController {

    @Resource
    AsyncServiceImpl asyncService;

    @RedisLimit
    @SaIgnore
    @GetMapping("/test")
    public Resp<Object> test() {
        return Resp.success();
    }

    @GetMapping("/t")
    public Resp<String> testAsync() {
        Future<String> test = asyncService.test();
        System.out.println(test.isDone());
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(test.isDone());
//        try {
//            String s = test.get();
//            System.out.println(s);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        System.out.println(test.isDone());
        return Resp.success();
    }
}
