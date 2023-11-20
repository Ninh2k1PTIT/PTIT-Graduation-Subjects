package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getByRoomId(Integer roomId);
    MessageDto create(MessageDto messageDto);
}
