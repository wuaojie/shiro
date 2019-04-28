package com.wuaojie.realm;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

public class shrioRealm {
//        DruidDataSource dataSource = new DruidDataSource();
//    {
//        dataSource.setUsername("root");
//        dataSource.setPassword("123456");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
//    }
    @Test
    public void shiroTest() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        //构建SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //new自定义realm
        myRealm myRealm = new myRealm();
        //设置自定义realm
        defaultSecurityManager.setRealm(myRealm);
        //new 加密对象 MD5加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("md5");
        //加密一次
        matcher.setHashIterations(1);
        //自定义set加密
        myRealm.setCredentialsMatcher(matcher);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("wuaojie", "123456");
        subject.login(usernamePasswordToken);
        System.out.printf("isRz:" + subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkPermissions("user:add","user:del");
    }
}
