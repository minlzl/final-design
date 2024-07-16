package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.finalend.constant.TalkRanking;
import com.example.finalend.utils.RedisUtil;
import com.example.finalend.vo.TalkVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService {

    @Resource
    RedisUtil redisUtil;

    @Resource
    AdminService adminService;

    public List<TalkVo> getRecommend(Long id) {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId != null) {
            return getRecommend((Long) loginId, 0, 20);
        } else {
            return coldStart(0, 20);
        }
    }

    public List<TalkVo> getRecommend(long loginId, int offset, int limit) {
        List<TalkVo> hot = coldStart(offset, limit);
        List<TalkVo> historyTalks = adminService.getHistoryTalks(loginId, (long) offset, (long) limit);
        List<TalkVo> rec = getRec(hot, historyTalks);
        return rec;

    }

    private List<TalkVo> getRec(List<TalkVo> hot, List<TalkVo> historyTalks) {
        List<TalkVo> rec = new ArrayList<TalkVo>(hot);
        for (TalkVo talkVo : hot) {
            if (historyTalks.contains(talkVo)) {
                rec.remove(talkVo);
            }
        }
        return rec;
    }

    public List<TalkVo> coldStart(int offset, int limit) {
        List<TalkVo> byScore = redisUtil.getByScore(TalkRanking.prefix, offset * limit, (offset + 1) * limit, TalkVo.class);
        return byScore;
    }



}
