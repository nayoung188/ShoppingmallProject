package com.shoppingmall.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MainController {

    // 메인페이지
    @GetMapping({"/",""})
    public String index (){
        return "index";
    }

    // 1:1 상담 페이지
    @GetMapping("/counselForm")
    public String counselForm(HttpServletRequest request, Model model){
        model.addAttribute("url", request.getRequestURL());
        return "/main/counselForm";
    }

}
