package com.example.finalend.async;

import java.util.concurrent.Future;

public interface IAsyncService {

    Future<String> test();
}
