package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.MessageDto;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.Room;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {
    public Message toEntity(MessageDto messageDto) {
        Message message = new Message();
        message.setContent(messageDto.getContent());

        Room room = new Room();
        room.setId(messageDto.getRoomId());
        message.setRoom(room);

        return message;
    }

    public MessageDto toDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setContent(message.getContent());
        messageDto.setRoomId(message.getRoom().getId());
        messageDto.setUserId(message.getUser().getId());
        messageDto.setCreatedAt(message.getCreatedAt());

        return messageDto;
    }
}
