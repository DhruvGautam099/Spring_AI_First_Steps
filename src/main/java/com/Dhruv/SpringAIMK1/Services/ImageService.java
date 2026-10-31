package com.Dhruv.SpringAIMK1.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ChatClient chatClient;
    private final String visionModel;

    public ImageService(OpenAiChatModel openAiChatModel,
                        @Value("${app.groq.vision-model}") String visionModel) {
        this.chatClient = ChatClient.create(openAiChatModel);
        this.visionModel = visionModel;
    }

    public String describeImage(MultipartFile file, String question) {
        MimeType mimeType = MimeTypeUtils.parseMimeType(file.getContentType());

        return chatClient.prompt()
                .options(OpenAiChatOptions.builder().model(visionModel))
                .user(u -> u.text(question)
                        .media(mimeType, file.getResource()))
                .call()
                .content();
    }
}