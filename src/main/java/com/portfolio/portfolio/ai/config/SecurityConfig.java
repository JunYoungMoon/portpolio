//package com.portfolio.portfolio.ai.config;
//
//import com.portfolio.portfolio.ai.application.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    public SecurityConfig(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers( "/auth/register", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/auth/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/dashboard", true)
//                        .failureUrl("/auth/login?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/auth/logout")
//                        .logoutSuccessUrl("/auth/login?logout=true")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
//                .sessionManagement(session -> session
//                        .maximumSessions(1) // 동시 세션 1개로 제한
//                        .maxSessionsPreventsLogin(false) // 새 로그인시 기존 세션 만료
//                )
//                .userDetailsService(userDetailsService);
//
//        return http.build();
//    }
//}
//
//
