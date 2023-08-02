package com.shoppingmall.config;


import com.shoppingmall.auth.AuthSuccessHandler;
import com.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 인가(접근권한) 설정
        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/dashboard/**").hasRole("MANAGER")
                .antMatchers("/**").permitAll();

        // 사이트 위변조 요청 방지
        http.csrf().disable();

        // 로그인 설정
        http.formLogin()
                .loginPage("/member/loginForm")
                .loginProcessingUrl("/member/loginForm")
                .usernameParameter("memberEmail")
                .passwordParameter("memberPw")
                .successHandler(authSuccessHandler);

        // 로그아웃 설정
        http.logout()
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID","remember-me");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}


