package com.example.finalend.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.Collections;
import java.util.List;

public class JsonUtil {
    public static <T> List<T> parseList(String json, Class<T> elementType) {
        List<T> list;
        try {
            JSONArray array = JSONUtil.parseArray(json);
            list = JSONUtil.toList(array, elementType);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return list == null ? Collections.emptyList() : list;
    }
}
