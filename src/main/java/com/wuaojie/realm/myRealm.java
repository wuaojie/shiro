package com.wuaojie.realm;

import com.wuaojie.cache.RedisCacheManager;
import com.wuaojie.dao.UserDao;
import com.wuaojie.dto.User;
import com.wuaojie.session.CustomSessionManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.Set;

public class myRealm extends AuthorizingRealm {
    @Autowired
    private RedisCacheManager cacheManager;

    @Autowired
    private UserDao userDao;
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String)principals.getPrimaryPrincipal();
        Set<String> rolesByusername = getRolesByusername(userName);
        Set<String> permissions = getPermissionsByName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(rolesByusername);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        PrincipalCollection previousPrincipals = SecurityUtils.getSubject().getPreviousPrincipals();
        System.out.println(previousPrincipals);
        if(null!=previousPrincipals){
            cacheManager.getCache("sun-cache:").remove(previousPrincipals);
        }
        return simpleAuthorizationInfo;
    }
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证开始");
        //获取前台传来的用户名
        String userName = (String) token.getPrincipal();
        //获取数据库密码
        User user= userDao.getPwdByUsername(userName);
        if(user==null){
            return null;
        }
        //创建返回对象
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), "shrioRealm");
        //返回认证对象时设置自定义盐
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("waj"));
        return simpleAuthenticationInfo;
    }

    //模拟数据库取得角色
    private Set<String> getRolesByusername(String userName) {
        System.out.println("从数据库获取");
        HashSet<String> strings = new HashSet<String>();
        strings.add("admin");
        strings.add("user");
        return strings;
    }
    //模拟取得权限
    private Set<String> getPermissionsByName(String userName) {
        System.out.println("从数据库获取");
        HashSet<String> strings = new HashSet<String>();
        strings.add("user:add");
        strings.add("user:del");
        return strings;
    }

    public static void main(String[] args) {
        //md5加密设置 waj盐
        Md5Hash md5 = new Md5Hash("123456","waj");
        System.out.printf(md5.toString());
    }
}
