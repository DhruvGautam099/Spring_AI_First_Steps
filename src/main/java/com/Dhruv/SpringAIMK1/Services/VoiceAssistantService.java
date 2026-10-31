package com.Dhruv.SpringAIMK1.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class VoiceAssistantService {

    private static final int MAX_CHARS = 200;

    private final ChatClient chatClient;
    private final TextToSpeechService ttsService;

    public VoiceAssistantService(OpenAiChatModel openAiChatModel,
                                 TextToSpeechService ttsService) {
        this.chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem("""
                        You are a voice assistant. Your reply will be read aloud.
                        Answer in at most 2 short sentences, under 180 characters total.
                        Use plain spoken English only: no markdown, no lists, no emojis, no code.
                        """)
                .build();
        this.ttsService = ttsService;
    }

    public String answer(String question) {
        String reply = chatClient.prompt()
                .user(question)
                .call()
                .content();
        return trimToLimit(reply);
    }

    public byte[] speak(String text, String voice) {
        return ttsService.speak(text, voice);
    }

    // safety net in case the model ignores the length instruction
    private String trimToLimit(String text) {
        if (text == null) return "";
        text = text.trim();
        if (text.length() <= MAX_CHARS) return text;

        String cut = text.substring(0, MAX_CHARS);
        int lastStop = Math.max(cut.lastIndexOf('.'),
                Math.max(cut.lastIndexOf('?'), cut.lastIndexOf('!')));
        return lastStop > 0 ? cut.substring(0, lastStop + 1) : cut;
    }
}