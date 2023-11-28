package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.MessageDto;
import com.example.socialnetwork.dto.MessagePhotoDto;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.MessagePhoto;
import com.example.socialnetwork.model.Room;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        messageDto.setPhotos(message.getMessagePhotos().stream().map(item -> {
            MessagePhotoDto messagePhotoDto = new MessagePhotoDto();
            messagePhotoDto.setId(item.getId());
            messagePhotoDto.setContent(item.getContent());
            return messagePhotoDto;
        }).collect(Collectors.toList()));

        return messageDto;
    }
}
