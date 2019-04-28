package com.wuaojie.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

public class CustomSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        //获取到sessionId
        Serializable sessionId = getSessionId(sessionKey);
        //定义request
        ServletRequest request = null;
        //如果sessionKey是WebSessionKey，则获取request
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        //从request获取session
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null)
                return session;
        }
        //没有从request获取到从缓存获取，下面这个方法继承DefaultWebSessionManager的
        //在spring注入了自定义的redisSessionDAO，所以从redis获取了
        Session session = super.retrieveSession(sessionKey);
        //如不为空存入request
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
