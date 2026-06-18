package com.example.exam.service.impl;

import com.example.exam.mapper.WrongQuestionMapper;
import com.example.exam.service.WrongQuestionService;
import com.example.exam.utils.SecurityUtils;
import com.example.exam.vo.WrongQuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WrongQuestionServiceImpl implements WrongQuestionService {

    private final WrongQuestionMapper wrongQuestionMapper;

    @Override
    public List<WrongQuestionVO> listMine() {
        return wrongQuestionMapper.selectByStudentId(SecurityUtils.getUserId());
    }
}

