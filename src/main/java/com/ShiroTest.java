package com;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mapperc")
public class ShiroTest {
    public static void main(String[] args) {

//        Subject subject = login();
//        if (subject.hasRole("role1")){
//            System.out.println("subject 拥有 role1");
//        }else{
//            System.out.println("subject 没有 role1");
//        }
//
//        subject.logout();
        SpringApplication.run(ShiroTest.class);
    }

    public static Subject login(){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("wang","456");
        try {
            subject.login(token);
            return subject;
        }catch (AuthenticationException ex){
            ex.printStackTrace();
            System.out.println("认证异常");
            return subject;
        }
    }
}


