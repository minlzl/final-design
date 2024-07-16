package com.example.finalend.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.finalend.entity.Reply;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.UserMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReplyVo implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long commentId;

    private String name;

    private String avatar;

    private LocalDateTime pubTime;

    private String content;

    public static ReplyVo of(Reply reply, UserMapper userMapper) {
        ReplyVo replyVo = new ReplyVo();
        BeanUtils.copyProperties(reply, replyVo);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", reply.getFromId()));
        if (user == null) {
            throw new ApiException("参数错误");
        }
        replyVo.setName(user.getName());
        replyVo.setAvatar(user.getAvatar());
        return replyVo;
    }
}
