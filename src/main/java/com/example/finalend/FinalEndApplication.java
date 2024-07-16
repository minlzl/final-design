package com.example.finalend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootApplication
@MapperScan("com.example.finalend.mapper")
@EnableCaching
public class FinalEndApplication implements CommandLineRunner {

    @Resource
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(FinalEndApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        String[] beans = applicationContext.getBeanDefinitionNames();
//        Arrays.sort(beans);
//        for (String bean : beans)
//        {
//            System.out.println(bean + " of Type :: " + applicationContext.getBean(bean).getClass());
//        }
    }
}
