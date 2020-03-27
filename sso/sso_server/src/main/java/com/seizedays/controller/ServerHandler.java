package com.seizedays.controller;

import com.seizedays.db.MockDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@Slf4j
public class ServerHandler {

    /**
     * 第一次登录验证
     * @param redirectUrl
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/checkToken")
    public String checkToken(String redirectUrl, HttpSession session, Model model, HttpServletRequest request){
        //获取token
        String token = (String) session.getServletContext().getAttribute("token");
        if(StringUtils.isEmpty(token)){
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        }else{
            //验证token
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if(cookie.getValue().equals(token)){
                    //验证通过，返回客户端
                    log.info("token验证通过");
                    return "redirect:"+redirectUrl+"?token="+token;
                }
            }
        }
        model.addAttribute("redirectUrl",redirectUrl);
        return "login";
    }

    @PostMapping("/login")
    public String login(String username,
                        String password,
                        String redirectUrl,
                        HttpSession session,
                        Model model){
        //判断登录
        if("admin".equals(username) && "123123".equals(password)){
            //1、创建token
            String token = UUID.randomUUID().toString();
            log.info("token创建成功！token={}",token);
            //2、token保存到全局会话中
            session.getServletContext().setAttribute("token",token);
            //3、token保存到数据库
            MockDB.tokenSet.add(token);
            //4、返回客户端
            return "redirect:"+redirectUrl+"?token="+token;
        }else{
            log.error("用户名密码错误！username={},password={}",username,password);
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        }
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token,String clientLogoutUrl){
        if(MockDB.tokenSet.contains(token)){
            Set<String> set = MockDB.clientLogoutUrlMap.get(token);
            if(set == null){
                set = new HashSet<>();
            }
            set.add(clientLogoutUrl);
            MockDB.clientLogoutUrlMap.put(token,set);
            return "true";
        }
        return "false";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

}
