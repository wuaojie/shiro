package com.wuaojie.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class roleFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        //获得主体
        Subject subject = getSubject(servletRequest, servletResponse);
        //请求需要的角色结果集
        String[] roles = (String[]) o;
        //有符合的就返回true
        for (String role: roles) {
            if(subject.hasRole(role))
                return true;
        }
        //如果需要角色为空则不需要权限，返回true
        if(roles==null || roles.length==0) {
            return true;
        }
        return  false;
    }
}
