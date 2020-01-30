package com.shiro;

import com.pojo.Permission;
import com.pojo.Role;
import com.pojo.User;
import com.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class UserRealm extends AuthorizingRealm {

   @Autowired
    UserService userService = null;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("principalCollection============================================================"+principalCollection.getPrimaryPrincipal());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        String username = (String) principalCollection.getPrimaryPrincipal();
        List<Role> roles = userService.selectUserRoles(username);
        List<Permission> permissions = userService.selectUserPermissions(username);


        Iterator<Role> iterator = roles.iterator();
        Iterator<Permission> ipermission = permissions.iterator();

        Set<String> roleSet = new LinkedHashSet<>();
        Set<String> perSet = new LinkedHashSet<>();
        while (iterator.hasNext()){
            Role role = iterator.next();
            roleSet.add(role.getName());
        }

        while (ipermission.hasNext()){
            perSet.add(ipermission.next().getPermissionName());
        }


        info.setRoles(roleSet);
        info.setStringPermissions(perSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String username = token.getUsername();

        User user = userService.selectUserByName(username);
        if (user == null) return null;

        return new SimpleAuthenticationInfo(username,user.getPassword(),"");
    }
}
