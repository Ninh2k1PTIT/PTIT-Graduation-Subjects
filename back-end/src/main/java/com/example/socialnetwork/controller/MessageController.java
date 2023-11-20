package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.MessageDto;
import com.example.socialnetwork.service.MessageService;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class MessageController {
    private SimpMessageSendingOperations messagingTemplate;
    private MessageService messageService;

    public MessageController(SimpMessageSendingOperations messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @PostMapping("/chat")
    @PreAuthorize("hasRole('USER')")
    public MessageDto send(@RequestBody MessageDto messageDto) {
        messagingTemplate.convertAndSend("/topic/" + messageDto.getRoomId(), messageDto);
        return messageService.create(messageDto);
    }

    @GetMapping("room/{roomId}/chats")
    public List<MessageDto> getByRoomId(@PathVariable Integer roomId) {
        return messageService.getByRoomId(roomId);
    }
}
