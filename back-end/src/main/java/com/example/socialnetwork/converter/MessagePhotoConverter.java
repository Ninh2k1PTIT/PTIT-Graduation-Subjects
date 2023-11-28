package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.MessagePhotoDto;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.MessagePhoto;
import org.springframework.stereotype.Component;

@Component
public class MessagePhotoConverter {
    public MessagePhoto toEntity(MessagePhotoDto messagePhotoDto) {
        MessagePhoto messagePhoto = new MessagePhoto();
        messagePhoto.setId(messagePhotoDto.getId());
        messagePhoto.setContent(messagePhotoDto.getContent());

        Message message = new Message();
        message.setId(messagePhotoDto.getMessageId());
        messagePhoto.setMessage(message);

        return messagePhoto;
    }

    public MessagePhotoDto toDto(MessagePhoto messagePhoto) {
        MessagePhotoDto messagePhotoDto = new MessagePhotoDto();
        messagePhotoDto.setId(messagePhoto.getId());
        messagePhotoDto.setContent(messagePhoto.getContent());
        messagePhotoDto.setMessageId(messagePhoto.getMessage().getId());
        return messagePhotoDto;
    }
}
