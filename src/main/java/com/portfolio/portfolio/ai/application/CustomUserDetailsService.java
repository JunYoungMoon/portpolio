//package com.portfolio.portfolio.ai.application;
//
//import com.portfolio.portfolio.ai.domain.repository.UserRepository;
//import com.portfolio.portfolio.ai.domain.model.entity.AIUser;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//
//@Service
//@Transactional(readOnly = true)
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        AIUser user = userRepository.findByUsername(username)
//                .filter(AIUser::isActive)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
//
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
//                .accountExpired(false)
//                .accountLocked(!user.isActive())
//                .credentialsExpired(false)
//                .disabled(!user.isActive())
//                .build();
//    }
//
//    // 추가: 사용자 정보를 가져오는 헬퍼 메서드
//    public AIUser getAIUser(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
//    }
//}