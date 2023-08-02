package com.shoppingmall.controller;

import com.shoppingmall.dto.MemberDTO;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 창 이동
    @GetMapping("/main/loginForm")
    public String loginForm(Model model,
                            @RequestParam(value="error", required = false)String error,
                            @RequestParam(value = "exception", required = false)String exception){
        model.addAttribute("error", error);
        model.addAttribute("exception",exception);
        return "/main/loginForm";
    }

    // 로그아웃
    @GetMapping("/member/logout")
    public String logoutForm(HttpServletRequest request,HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";
    }


    // 회원가입 창 이동
    @GetMapping("/member/joinForm")
    public String joinForm(){
        return "main/joinForm";
    }

    @PostMapping("/member/join")
    public String join(@ModelAttribute @Valid MemberDTO memberDTO){
        memberService.saveMember(memberDTO);
        return "redirect:/";
    }

    // 이메일 중복 확인
    @PostMapping("/member/checkEmail")
    @ResponseBody
    public String checkSameEmail(@RequestParam("memberEmail")String memberEmail){
        return memberService.checkSameEmail(memberEmail);
    }

    // 핸드폰 문자 인증
    @PostMapping("/sms/sendAuthCode")
    @ResponseBody
    public String sendAuthCode(@RequestBody String memberMobile){
        String authCode = memberService.sendAuthCodeService(memberMobile);
        return authCode;
    }


}
