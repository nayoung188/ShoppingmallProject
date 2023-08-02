package com.shoppingmall.controller;

import com.shoppingmall.util.SmartStoreUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    @Autowired
    private SmartStoreUtil smartStoreUtil;

    @GetMapping("/dashboard/orderList")
    public String orderList(Model model){
        String orderList = smartStoreUtil.getOrderList();
        model.addAttribute("orderList" ,orderList);
        System.out.println("컨트롤러단 ::: " + orderList);
        return "/dashboard/order/orderList";
    }

    @PostMapping("/orderDetail")
    @ResponseBody
    public String gerOrderDetail(@RequestParam("orderIds")String orderIds){
        JSONArray orderDetails = smartStoreUtil.getOrderDetail(orderIds);
        return orderDetails.toString();
    }

    @PostMapping("/naverProductList")
    @ResponseBody
    public String productList(){
        String productList = smartStoreUtil.getProductList();
        return productList;
    }

    @PostMapping("/naver/addProduct")
    public String addProduct(String addInformation){
        smartStoreUtil.addProduct(addInformation);
        return "redirect:/";
    }
}
