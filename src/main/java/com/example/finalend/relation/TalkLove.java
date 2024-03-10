package com.example.finalend.relation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class TalkLove {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long talkId;

    private Long userId;
}
