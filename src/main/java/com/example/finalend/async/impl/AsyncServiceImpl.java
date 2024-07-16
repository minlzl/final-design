package com.example.finalend.async.impl;

import com.example.finalend.async.IAsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncServiceImpl implements IAsyncService {

    @Async("asyncServiceExecutor")
    @Override
    public Future<String> test() {
        String ans;
        try {
            Thread.sleep(5000);
            ans = "success";
        } catch (InterruptedException e) {
            e.printStackTrace();
            ans = "error";
        }
        return AsyncResult.forValue(ans);
    }
}
