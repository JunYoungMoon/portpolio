package com.portfolio.portfolio.ai.application;

import com.portfolio.portfolio.ai.domain.VoiceHistoryRepository;
import com.portfolio.portfolio.ai.presentation.VoiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoiceService {

    private final VoiceHistoryRepository voiceHistoryRepository;

    public void processVoiceTest(VoiceRequest request) {

        String text = request.getText().trim();

    }
}
