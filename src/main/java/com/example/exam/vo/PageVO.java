package com.example.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<T> records;
}

