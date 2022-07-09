package com.wuaojie.controller;

import com.wuaojie.dto.User;
import com.wuaojie.util.JedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class shiroTestController {

    private final String CACHE_PREFIX = "sun-cache:";
    @Autowired
    private JedisUtil jedisUtil;

    @RequestMapping(value = "/subLogin", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String test(User user) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            usernamePasswordToken.setRememberMe(user.getRemember());
            subject.login(usernamePasswordToken);
            return "登陆成功！";
        } catch (Exception e) {
            return "登陆失败";
        }
    }

    @RequestMapping(value = "/test", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String test2(User user) {
        try {
            Subject subject = SecurityUtils.getSubject();
            boolean authenticated = subject.isAuthenticated();
            System.out.printf("是否通过：" + authenticated);
            return "注册成功";
        } catch (Exception e) {
            return "注册失败";
        }
    }

    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String testRole() {
        return "对";
    }

    @RequiresRoles("admin1")
    @RequestMapping(value = "/testRole1", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String testRole1() {
        return "有admin1权限";
    }

    //自定义filter测试多角色的
    @RequestMapping(value = "/roles", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String roles() {
        return "有admin权限";
    }

    @RequiresRoles("admin")
    @RequestMapping(value = "/delredis", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String delRedis() {
        Subject subject = SecurityUtils.getSubject();
//        subject.
//        PrincipalCollection previousPrincipals = SecurityUtils.getSubject().getPrincipals();

        return "对";
    }
}
