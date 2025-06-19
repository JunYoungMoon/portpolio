package com.portfolio.portfolio.ai.domain.model;

import lombok.Getter;

@Getter
public class AIPrompt {
    private final String prompt;
    private final String context;
    private final String userCommand;

    private AIPrompt(String prompt, String context, String userCommand) {
        this.prompt = prompt;
        this.context = context;
        this.userCommand = userCommand;
    }

    public static AIPrompt create(String userCommand, String username) {
        String context = String.format("사용자 '%s'의 음성 명령입니다.", username);
        String prompt = buildPrompt(userCommand, context);
        return new AIPrompt(prompt, context, userCommand);
    }

    private static String buildPrompt(String userCommand, String context) {
        return String.format("""
            %s
            
            사용자 명령: "%s"
            
            위 명령을 이해하고 적절한 응답을 제공해주세요.
            응답은 친근하고 도움이 되는 톤으로 작성해주세요.
            """, context, userCommand);
    }
}