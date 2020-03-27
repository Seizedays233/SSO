package com.seizedays.controller;

import com.seizedays.util.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TaobaoHandler {

    @GetMapping("/taobao")
    public String index(Model model){
        model.addAttribute("serverLogoutUrl",SSOClientUtil.getServerLogoutUrl());
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/taobao";
    }

}
