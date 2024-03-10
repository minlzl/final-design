package com.example.finalend.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.finalend.entity.Follow;
import com.example.finalend.entity.Talk;
import com.example.finalend.entity.User;
import com.example.finalend.mapper.TalkImgMapper;
import com.example.finalend.mapper.TalkLoveMapper;
import com.example.finalend.mapper.TalkTagMapper;
import com.example.finalend.relation.TalkImg;
import com.example.finalend.relation.TalkLove;
import com.example.finalend.relation.TalkTag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TalkVo implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String userName;

    private LocalDateTime publishTime;

    private String content;

    private String avatar;

    private Integer commentNumber;

    private Integer loveNumber;

    private Boolean love;

    private Boolean self;

    private Boolean follow;

    private List<String> images;

    private List<String> tags;

    public static TalkVo of(Talk talk, Boolean love, Boolean self, Boolean follow, String name, String avatar, List<String> images, List<String> tags) {
        TalkVo talkVo = new TalkVo();
        BeanUtils.copyProperties(talk, talkVo);
        talkVo.setTags(tags);
        talkVo.setSelf(self);
        talkVo.setImages(images);
        talkVo.setLove(love);
        talkVo.setFollow(follow);
        talkVo.setUserName(name);
        talkVo.setAvatar(avatar);
        return talkVo;
    }
}
