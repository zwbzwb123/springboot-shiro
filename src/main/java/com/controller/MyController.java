package com.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;


@Controller
public class MyController {

    @RequiresRoles("role1")

    @RequestMapping("add")
    public String add(){
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.hasRole("role2")){
//            System.out.println("2222222222222222222222222222222222222");
//        }
        return "add";
    }

    @RequiresRoles("role2")
    @RequestMapping("update")
    public String update(Model model){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("user:update")) {
            return "update";
        }else{
            model.addAttribute("msg","权限不足");
            return "login";
        }
    }

    @RequiresPermissions("user:view")
    @RequestMapping("view")
    public String view(){
        return "list";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){

        System.out.println("name:"+username);
        System.out.println("pwd:"+password);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        try {
            token.setRememberMe(true);
            subject.login(token);
        }catch (UnknownAccountException ex){
            model.addAttribute("msg","用户名不存在");
            return "login";
        }catch (IncorrectCredentialsException ex){
            model.addAttribute("msg","密码错误");
            return "login";
        }

        return "hello";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }


    @RequestMapping("/show")
    public String show(HttpSession session){
        System.out.println("show=========");
        Enumeration<String> enumeration = session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            // 获取session键值
            String name = enumeration.nextElement().toString();
            // 根据键值取session中的值
            Object value = session.getAttribute(name);
            // 打印结果
            System.out.println("<B>" + name + "</B>=" + value + "<br>/n");
        }

        return "hello";
    }

    @RequestMapping("/out")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
