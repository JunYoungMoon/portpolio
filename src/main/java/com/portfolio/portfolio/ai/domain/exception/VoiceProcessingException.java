package com.portfolio.portfolio.ai.domain.exception;

public class VoiceProcessingException extends RuntimeException{
    public VoiceProcessingException(String message) {
        super(message);
    }

    public VoiceProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
