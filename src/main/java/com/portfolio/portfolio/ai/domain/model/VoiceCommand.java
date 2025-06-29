package com.portfolio.portfolio.ai.domain.model;


import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class VoiceCommand {

    private final String text;
    private final String username;
    private final LocalDateTime timestamp;
    private final String id;

    private VoiceCommand(String text, String username, LocalDateTime timestamp) {
        this.text = validateText(text);
        this.username = validateUsername(username);
        this.timestamp = timestamp;
        this.id = generateId();
    }

    private String validateText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("음성 텍스트는 비어있을 수 없습니다.");
        }
        return text.trim();
    }

    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("사용자명은 비어있을 수 없습니다.");
        }
        return username.trim();
    }

    private String generateId() {
        return String.format("%s_%d", username, System.currentTimeMillis());
    }

    public static VoiceCommand create(String text, String username, String timestampStr) {
        LocalDateTime timestamp = parseTimestamp(timestampStr);
        return new VoiceCommand(text, username, timestamp);
    }

    private static LocalDateTime parseTimestamp(String timestampStr) {
        try {
            return LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    public boolean hasContent() {
        return text != null && !text.trim().isEmpty();
    }
}
