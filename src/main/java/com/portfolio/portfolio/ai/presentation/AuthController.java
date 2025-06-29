package com.portfolio.portfolio.ai.presentation;

import com.portfolio.portfolio.ai.application.UserRegistrationRequest;
import com.portfolio.portfolio.ai.application.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        model.addAttribute("loginRequest", new LoginRequest("", ""));

        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (logout != null) {
            model.addAttribute("message", "성공적으로 로그아웃되었습니다.");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest("", "", "", ""));
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            UserRegistrationRequest request = new UserRegistrationRequest(
                    registerRequest.username(),
                    registerRequest.email(),
                    registerRequest.password(),
                    registerRequest.name()
            );

            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/auth/login";

        } catch (IllegalArgumentException e) {
            result.rejectValue("username", "register.failed", e.getMessage());
            return "auth/register";
        }
    }
}
