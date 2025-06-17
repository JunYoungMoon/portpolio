package com.portfolio.portfolio.ai.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


//TODO 왜 이 애너테이션이 필요한가?
@Setter
@Getter
@AllArgsConstructor
public class VoiceRequest {
    private String text;
    private String timestamp;
}
