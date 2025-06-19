package com.portfolio.portfolio.ai.presentation;


import com.portfolio.portfolio.ai.domain.exception.VoiceProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VoiceExceptionHandler {

    @ExceptionHandler(VoiceProcessingException.class)
    public ResponseEntity<ErrorResponse> handleVoiceProcessingException(VoiceProcessingException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("음성 처리 오류", e.getMessage()));
    }

//    @ExceptionHandler(AIClientException.class)
//    public ResponseEntity<ErrorResponse> handleAIClientException(AIClientException e) {
//        return ResponseEntity.internalServerError()
//                .body(new ErrorResponse("AI 서비스 오류", e.getMessage()));
//    }

    public static class ErrorResponse {
        public final String error;
        public final String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
    }
}
