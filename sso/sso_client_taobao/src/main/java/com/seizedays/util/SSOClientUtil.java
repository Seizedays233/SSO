package com.seizedays.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class SSOClientUtil {
    private static Properties properties = new Properties();
    public static String SERVER_HOST_URL;
    public static String CLIENT_HOST_URL;

    static {
        try {
            properties.load(SSOClientUtil.class.getClassLoader().getResourceAsStream("sso.properties"));
            SERVER_HOST_URL = properties.getProperty("server.host.url");
            CLIENT_HOST_URL = properties.getProperty("client.host.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //跳转到认证中心
    public static void redirectToCheckToken(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer url = new StringBuffer();
        url.append(SERVER_HOST_URL)
                .append("/checkToken?redirectUrl=")
                .append(CLIENT_HOST_URL)
                .append(request.getServletPath());
        try {
            response.sendRedirect(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServerLogoutUrl(){
        return SERVER_HOST_URL+"/logout";
    }

    public static String getClientLogoutUrl(){
        return CLIENT_HOST_URL+"/logout";
    }
}
