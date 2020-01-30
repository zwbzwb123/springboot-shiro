package com.service;

import com.pojo.Permission;
import com.pojo.Role;
import com.pojo.User;

import java.util.List;

public interface UserService {

    User selectUserByName(String name);

    List<Role> selectUserRoles(String username);

    List<Permission> selectUserPermissions(String username);
}
