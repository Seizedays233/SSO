package com.seizedays.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {
    /**
     * 用于发送异步请求的工具类
     * id=1
     * name=tom
     * {id=1,name=tom} id=1&name=tom
     * @param httpUrl
     * @param params
     * @return
     */
    public static String sendHttpRequest(String httpUrl, Map<String,String> params){
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            if(params!=null && params.size()>0){
                StringBuffer stringBuffer = new StringBuffer();
                //拼接请求信息
                for(Map.Entry<String,String> entry:params.entrySet()){
                    stringBuffer.append("&")
                            .append(entry.getKey())
                            .append("=")
                            .append(entry.getValue());
                }
                connection.getOutputStream().write(stringBuffer.substring(1).toString().getBytes("UTF-8"));
            }
            //发送请求
            connection.connect();
            String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
