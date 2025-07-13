//package com.portfolio.portfolio.ai.application;
//
//import com.portfolio.portfolio.ai.domain.model.entity.AIUser;
//import com.portfolio.portfolio.ai.domain.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public AIUser registerUser(UserRegistrationRequest request) {
//        validateRegistration(request);
//
//        String encodedPassword = passwordEncoder.encode(request.password());
//        AIUser user = new AIUser(request.username(), request.email(), encodedPassword, request.name());
//
//        return userRepository.save(user);
//    }
//
//    // authenticate 메서드 제거 - Spring Security가 인증 처리
//
//    @Transactional(readOnly = true)
//    public Optional<AIUser> findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<AIUser> findById(Long id) {
//        return userRepository.findById(id);
//    }
//
//    private void validateRegistration(UserRegistrationRequest request) {
//        if (userRepository.existsByUsername(request.username())) {
//            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
//        }
//
//        if (userRepository.existsByEmail(request.email())) {
//            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
//        }
//    }
//}
//
