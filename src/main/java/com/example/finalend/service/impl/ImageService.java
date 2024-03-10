package com.example.finalend.service.impl;

import com.example.finalend.config.FinalDesignProperty;
import com.example.finalend.exception.ApiException;
import com.example.finalend.utils.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService {

    @Resource
    FinalDesignProperty finalDesignProperty;

    @Resource
    SmmsService smmsService;

    public String save(MultipartFile multipartFile) {
        String folder = finalDesignProperty.getFolder();
        Integer fileSize = finalDesignProperty.getFileSize();
        String fileName = StrUtil.getRandomString(fileSize);
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        System.out.println(folder + " " + fileName + " " + suffix);
        File file = new File(folder);
        System.out.println(file.getPath());
        File f = new File(file, fileName + suffix);
        System.out.println(f.getPath());
        try {
            multipartFile.transferTo(f);
            return smmsService.upload(f.getPath());
        } catch (IOException e) {
            throw new ApiException("图片上传错误");
        }
    }
}
