package com.example.finalend.config;

import com.example.finalend.annotation.AccessLimit;
import com.example.finalend.annotation.ApiRestController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer, HandlerInterceptor {

    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(ApiRestController.class));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }


//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod hm = (HandlerMethod) handler;
//            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
//            if (accessLimit != null) {
//
//            }
//        }
//    }
}
