package com.mapper;

import com.pojo.Permission;
import com.pojo.Role;
import com.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectUserByName(String name);

    List<Role> selectUserRoles(String username);

    List<Permission> selectUserPermissions(String username);

}
