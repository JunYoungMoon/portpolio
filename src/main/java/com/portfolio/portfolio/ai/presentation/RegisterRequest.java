package com.portfolio.portfolio.ai.presentation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegisterRequest(
        @NotBlank(message = "사용자명을 입력해주세요.")
        @Size(min = 3, max = 20, message = "사용자명은 3-20자 사이여야 합니다.")
        String username,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 2, max = 10, message = "이름은 2-10자 사이여야 합니다.")
        String name
) {}
