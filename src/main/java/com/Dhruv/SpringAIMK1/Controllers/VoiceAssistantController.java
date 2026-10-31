package com.Dhruv.SpringAIMK1.Controllers;

import com.Dhruv.SpringAIMK1.Services.VoiceAssistantService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/voice")
public class VoiceAssistantController {

    private final VoiceAssistantService voiceService;

    public VoiceAssistantController(VoiceAssistantService voiceService) {
        this.voiceService = voiceService;
    }

    public record AskRequest(String question, String voice) {}

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody AskRequest request) {
        if (request.question() == null || request.question().isBlank()) {
            return ResponseEntity.badRequest().body("Question is required");
        }

        String answerText = voiceService.answer(request.question());
        if (answerText.isBlank()) {
            return ResponseEntity.internalServerError().body("Model returned an empty answer");
        }

        byte[] audio = voiceService.speak(answerText, request.voice());

        return ResponseEntity.ok()
                .header("X-Answer-Text", URLEncoder.encode(answerText, StandardCharsets.UTF_8))
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "X-Answer-Text")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=answer.wav")
                .contentType(MediaType.valueOf("audio/wav"))
                .body(audio);
    }
}