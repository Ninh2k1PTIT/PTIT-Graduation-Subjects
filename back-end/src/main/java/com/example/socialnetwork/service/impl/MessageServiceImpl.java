package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.MessageConverter;
import com.example.socialnetwork.dto.MessageDto;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.repository.MessageRepository;
import com.example.socialnetwork.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;
    private MessageConverter messageConverter;

    @Override
    public List<MessageDto> getByRoomId(Integer roomId) {
        return messageRepository.findByRoomId(roomId).stream().map(item -> messageConverter.toDto(item)).collect(Collectors.toList());
    }

    @Override
    public MessageDto create(MessageDto messageDto) {
        Message message = messageConverter.toEntity(messageDto);
        return messageConverter.toDto(messageRepository.save(message));
    }
}
