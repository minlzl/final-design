package com.example.finalend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.finalend.entity.Talk;
import com.example.finalend.param.TalkParam;
import com.example.finalend.vo.TalkVo;

import java.time.LocalDateTime;
import java.util.List;

public interface ITalkService extends IService<Talk> {
    boolean saveTalk(TalkParam talkParam);

    List<TalkVo> getTalks(Long id);

    void addFollow(TalkParam talkParam);

    List<TalkVo> getHotTalk(Long id);
}
