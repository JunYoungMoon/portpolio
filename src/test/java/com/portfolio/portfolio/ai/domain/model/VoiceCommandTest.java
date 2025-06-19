package com.portfolio.portfolio.ai.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class VoiceCommandTest {

    @Test
    void 음성_명령_생성_성공() {
        // Given
        String text = "안녕하세요";
        String username = "testuser";
        String timestamp = "2025-06-20T10:00:00";

        // When
        VoiceCommand command = VoiceCommand.create(text, username, timestamp);

        // Then
        assertThat(command.getText()).isEqualTo("안녕하세요");
        assertThat(command.hasContent()).isTrue();
    }

    @Test
    void 빈_텍스트는_예외_발생() {
        // When & Then
        assertThatThrownBy(() ->
                VoiceCommand.create("", "user", "2025-06-20T10:00:00")
        ).isInstanceOf(IllegalArgumentException.class);
    }
}