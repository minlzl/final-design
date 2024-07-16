package com.example.finalend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "final-design")
public class FinalDesignProperty {
    private String test;
    private Integer talkSize;
    private String folder;
    private Integer fileSize;
    private String smmsToken;
}
