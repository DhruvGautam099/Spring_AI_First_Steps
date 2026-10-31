package com.Dhruv.SpringAIMK1.Controllers;

import com.Dhruv.SpringAIMK1.Services.OllamaChatService;
import com.Dhruv.SpringAIMK1.Services.OpenAiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private OpenAiChatService openAiChatService;

    @Autowired
    private OllamaChatService ollamaChatService;

    @PostMapping("/openAi/generate")
    public String generateTextOpenAI(@RequestBody String prompt) {
        return openAiChatService.generateText(prompt);
    }

    @PostMapping("/ollama/generate")
    public String generateTextOllama(@RequestBody String prompt){
        return ollamaChatService.generateText(prompt);
    }


}