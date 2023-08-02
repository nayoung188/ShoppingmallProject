package com.shoppingmall.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

    @GetMapping("/dashboard/main")
    public String dashboardMain(){
        return "dashboard/main";
    }


}
