package com.example.exam.mapper;

import com.example.exam.dto.LoginLogQueryRequest;
import com.example.exam.entity.LoginLog;
import com.example.exam.vo.LoginLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginLogMapper {

    int insert(LoginLog loginLog);

    long countByQuery(@Param("query") LoginLogQueryRequest query);

    List<LoginLogVO> selectPage(@Param("query") LoginLogQueryRequest query,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);
}
