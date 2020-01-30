package com.service.impl;

import com.mapper.UserMapper;
import com.pojo.Permission;
import com.pojo.Role;
import com.pojo.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserMapper userMapper = null;

    @Override
    public User selectUserByName(String name) {
        return userMapper.selectUserByName(name);
    }

    @Override
    public List<Role> selectUserRoles(String username) {
        return userMapper.selectUserRoles(username);
    }

    @Override
    public List<Permission> selectUserPermissions(String username) {
        return userMapper.selectUserPermissions(username);
    }
}
