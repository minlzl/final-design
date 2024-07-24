package com.example.finalend.config;

import com.example.finalend.constant.RabbitmqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean(RabbitmqConstant.SECKILL_ORDER)
    public Queue order() {
        return new Queue(RabbitmqConstant.SECKILL_ORDER, true);
    }

    @Bean("order.exchange")
    public TopicExchange topicExchange() {
        return new TopicExchange("order.exchange", true, false);
    }

    @Bean
    public Binding bindingQueue() {
        return BindingBuilder.bind(order()).to(topicExchange()).with("order");
    }
}
