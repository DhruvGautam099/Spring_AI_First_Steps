package com.Dhruv.SpringAIMK1.Services;

import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {

    private final OpenAiAudioSpeechModel speechModel;
    private final String model;
    private final String defaultVoice;

    public TextToSpeechService(OpenAiAudioSpeechModel speechModel,
                               @Value("${app.groq.tts-model}") String model,
                               @Value("${app.groq.tts-voice}") String defaultVoice) {
        this.speechModel = speechModel;
        this.model = model;
        this.defaultVoice = defaultVoice;
    }

    public byte[] speak(String text, String voice) {
        String useVoice = (voice == null || voice.isBlank()) ? defaultVoice : voice;

        TextToSpeechPrompt prompt = new TextToSpeechPrompt(
                text,
                OpenAiAudioSpeechOptions.builder()
                        .model(model)
                        .voice(useVoice)
                        .responseFormat("wav")
                        .build());

        return speechModel.call(prompt).getResult().getOutput();
    }
}