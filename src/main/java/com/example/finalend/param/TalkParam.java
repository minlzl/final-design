package com.example.finalend.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class TalkParam {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String content;

    private List<String> tags;

    private List<String> images;
}
