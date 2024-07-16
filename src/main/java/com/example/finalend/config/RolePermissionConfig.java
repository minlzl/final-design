package com.example.finalend.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolePermissionConfig implements StpInterface {
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return null;
    }
}
