package com.seizedays.listener;

import com.seizedays.db.MockDB;
import com.seizedays.util.HttpUtil;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Iterator;
import java.util.Set;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //1、删除全局会话中的token
        String token = (String) se.getSession().getServletContext().getAttribute("token");
        se.getSession().getServletContext().removeAttribute("token");

        //2、删除数据库的用户信息
        MockDB.tokenSet.remove(token);

        //3、通知所有客户端销毁session
        Set<String> set = MockDB.clientLogoutUrlMap.get(token);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            HttpUtil.sendHttpRequest(iterator.next(),null);
        }
        MockDB.clientLogoutUrlMap.remove(token);
    }
}
