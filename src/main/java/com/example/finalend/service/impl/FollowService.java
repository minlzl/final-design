package com.example.finalend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.finalend.entity.Follow;
import com.example.finalend.mapper.FollowMapper;
import com.example.finalend.service.IFollowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    FollowMapper followMapper;

    @Override
    public List<Long> getFollowIds(Long id) {
        List<Follow> follows = followMapper.selectList(new QueryWrapper<Follow>().eq("user_id", id));
        List<Long> ids = new ArrayList<>();
        for (Follow follow : follows) {
            ids.add(follow.getFollowId());
        }
        return ids;
    }
}
