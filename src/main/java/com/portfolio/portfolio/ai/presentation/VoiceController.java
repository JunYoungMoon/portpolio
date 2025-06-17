package com.portfolio.portfolio.ai.presentation;


import com.portfolio.portfolio.ai.application.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voice")
@RequiredArgsConstructor
public class VoiceController {

    private final VoiceService voiceService;

    @PostMapping("/process")
    public ResponseEntity<VoiceResponse> processVoiceData(@RequestBody VoiceRequest request){
        voiceService.processVoiceTest(request);
    }
}
