package com.example.finalend.service.impl;

import cn.hutool.json.JSONObject;
import com.example.finalend.config.FinalDesignProperty;
import com.example.finalend.exception.ApiException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

@Service
public class SmmsService {

    String reqUrl = "https://sm.ms/api/v2/upload";

    @Resource
    RestTemplate restTemplate;

    @Resource
    FinalDesignProperty finalDesignProperty;

    public String upload(String filePath) {
        System.out.println("into smms");
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        params.add("smfile", resource);
        headers.setBasicAuth(finalDesignProperty.getSmmsToken());
        headers.set("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        System.out.println(headers);
        int count = 3;
        JSONObject entries = new JSONObject();
        String url = null;
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
        while (count-- > 0) {
            try {
                entries = restTemplate.postForObject(reqUrl, request, JSONObject.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            System.out.println(entries);
            if (Boolean.TRUE.equals(entries.get("success"))) {
                url = entries.get("data", JSONObject.class).get("url", String.class);
                break;
            } else if (entries.containsKey("images")){
                url = entries.get("images", String.class);
                break;
            } else {
                System.out.println("上传失败，重传");
            }
        }
        if (count == 0) {
            throw new ApiException("图片上传失败");
        }
        System.out.println(url);
        return url;
    }
}
