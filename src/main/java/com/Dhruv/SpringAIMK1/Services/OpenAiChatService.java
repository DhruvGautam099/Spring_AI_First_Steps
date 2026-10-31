package com.Dhruv.SpringAIMK1.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiChatService {

    private final ChatClient chatClient;

    public OpenAiChatService(OpenAiChatModel openAiChatModel) {
        this.chatClient = ChatClient.builder(openAiChatModel).build();
    }

    public String generateText(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}