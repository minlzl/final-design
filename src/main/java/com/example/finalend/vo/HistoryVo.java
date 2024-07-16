package com.example.finalend.vo;

import com.example.finalend.entity.History;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryVo {

    private String userName;

    private String ipAddr;

    private String type;

    private String content;

    private LocalDateTime viewTime;

    public static HistoryVo of(History history, String userName, String content) {
        HistoryVo historyVo = new HistoryVo();
        BeanUtils.copyProperties(history, historyVo);
        historyVo.setUserName(userName);
        historyVo.setViewTime(history.getCreateTime());
        historyVo.setContent(content);
        return historyVo;
    }
}
