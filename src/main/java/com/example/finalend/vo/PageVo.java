package com.example.finalend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {

    private Collection<T> records;
    private Long size;
    private Long current;
    private Long total;

}
