package com.example.exam.mapper;

import com.example.exam.entity.SysUser;
import com.example.exam.dto.UserQueryRequest;
import com.example.exam.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    SysUser selectByUsername(@Param("username") String username);

    SysUser selectByEmail(@Param("email") String email);

    SysUser selectById(@Param("id") Long id);

    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    long countUsers(@Param("query") UserQueryRequest query);

    List<UserVO> selectUsers(@Param("query") UserQueryRequest query,
                             @Param("offset") Integer offset,
                             @Param("limit") Integer limit);

    int insert(SysUser user);

    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteUserRoles(@Param("userId") Long userId);
}
