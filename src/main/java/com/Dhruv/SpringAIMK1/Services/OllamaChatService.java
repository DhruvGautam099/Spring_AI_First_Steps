package com.Dhruv.SpringAIMK1.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaChatService{

    private final ChatClient chatClient;

    public OllamaChatService (OllamaChatModel ollamaChatModel){
        this.chatClient=ChatClient.builder(ollamaChatModel).build();
    }

    public String generateText(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

}
