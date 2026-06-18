package com.example.exam.mapper;

import com.example.exam.entity.SysRole;
import com.example.exam.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {

    SysRole selectByCode(@Param("roleCode") String roleCode);

    List<RoleVO> selectAll();
}
