package com.example.exam.service.impl;

import com.example.exam.common.BusinessException;
import com.example.exam.config.AuthProperties;
import com.example.exam.dto.LoginLogQueryRequest;
import com.example.exam.dto.UserQueryRequest;
import com.example.exam.dto.UserRolesUpdateRequest;
import com.example.exam.dto.UserStatusRequest;
import com.example.exam.entity.SysRole;
import com.example.exam.entity.SysUser;
import com.example.exam.mapper.LoginLogMapper;
import com.example.exam.mapper.RoleMapper;
import com.example.exam.mapper.UserMapper;
import com.example.exam.service.UserService;
import com.example.exam.vo.LoginLogVO;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.RoleVO;
import com.example.exam.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final LoginLogMapper loginLogMapper;
    private final StringRedisTemplate redisTemplate;
    private final AuthProperties authProperties;

    @Override
    public PageVO<UserVO> users(UserQueryRequest request) {
        int pageNum = request.getPageNum() == null ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        long total = userMapper.countUsers(request);
        List<UserVO> records = total == 0 ? List.of() : userMapper.selectUsers(request, offset, pageSize);
        for (UserVO record : records) {
            record.setRoles(userMapper.selectRoleCodesByUserId(record.getId()));
        }
        return new PageVO<>(total, pageNum, pageSize, records);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, UserStatusRequest request) {
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessException(400, "用户状态必须为0或1");
        }
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        userMapper.updateStatus(id, request.getStatus());
        if (request.getStatus() == 0) {
            Set<String> keys = redisTemplate.keys(authProperties.getRedisTokenPrefix() + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    @Override
    public List<RoleVO> roles() {
        return roleMapper.selectAll();
    }

    @Override
    @Transactional
    public void updateRoles(Long id, UserRolesUpdateRequest request) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        userMapper.deleteUserRoles(id);
        for (String roleCode : request.getRoleCodes()) {
            SysRole role = roleMapper.selectByCode(roleCode);
            if (role == null) {
                throw new BusinessException(400, "角色不存在：" + roleCode);
            }
            userMapper.insertUserRole(id, role.getId());
        }
    }

    @Override
    public PageVO<LoginLogVO> loginLogs(LoginLogQueryRequest request) {
        int pageNum = request.getPageNum() == null ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        long total = loginLogMapper.countByQuery(request);
        List<LoginLogVO> records = total == 0 ? List.of() : loginLogMapper.selectPage(request, offset, pageSize);
        return new PageVO<>(total, pageNum, pageSize, records);
    }
}
