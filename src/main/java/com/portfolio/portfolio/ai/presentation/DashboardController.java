//package com.portfolio.portfolio.ai.presentation;
//
//import com.portfolio.portfolio.ai.application.CustomUserDetailsService;
//import com.portfolio.portfolio.ai.domain.model.entity.AIUser;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class DashboardController {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    public DashboardController(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @GetMapping({"/", "/dashboard"})
//    public String dashboard(Model model) {
//        // Spring Security에서 현재 인증된 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        // AIUser 객체 가져오기
//        AIUser user = userDetailsService.getAIUser(username);
//
//        model.addAttribute("user", user);
//        return "dashboard/index";
//    }
//}