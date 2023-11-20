package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.MessageDto;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class MessageController {
    private SimpMessageSendingOperations messagingTemplate;

    public MessageController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/chat")
    @PreAuthorize("hasRole('USER')")
    public boolean send(@RequestBody MessageDto message) {
        messagingTemplate.convertAndSend("/topic/" + message.getGroupId(), message);
        return true;
    }
}
