package tn.esprit.clubconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.ChatMessage;
import tn.esprit.clubconnect.services.ChatBotService;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/chatbot")
public class ChatBotController {

    private final ChatBotService chatBotService;

    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/respond")
    public CompletableFuture<ResponseEntity<ChatMessage>> respond(@RequestBody ChatMessage message) {
        // Generate a response asynchronously with a 2-second delay
        return chatBotService.generateResponse(message)
                .thenApply(response -> ResponseEntity.ok(response)); // Wrap the response in ResponseEntity
    }
}