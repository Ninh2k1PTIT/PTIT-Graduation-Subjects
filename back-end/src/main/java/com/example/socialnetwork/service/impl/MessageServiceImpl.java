package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.MessageConverter;
import com.example.socialnetwork.converter.MessagePhotoConverter;
import com.example.socialnetwork.dto.MessageDto;
import com.example.socialnetwork.dto.MessagePhotoDto;
import com.example.socialnetwork.model.Message;
import com.example.socialnetwork.model.MessagePhoto;
import com.example.socialnetwork.model.Room;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.repository.MessagePhotoRepository;
import com.example.socialnetwork.repository.MessageRepository;
import com.example.socialnetwork.repository.RoomRepository;
import com.example.socialnetwork.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;
    private MessagePhotoRepository messagePhotoRepository;
    private RoomRepository roomRepository;
    private MessageConverter messageConverter;
    private MessagePhotoConverter messagePhotoConverter;
    private SimpMessageSendingOperations messagingTemplate;

    @Override
    public List<MessageDto> getByRoomId(Integer roomId) {
        return messageRepository.findByRoomId(roomId).stream().map(item -> messageConverter.toDto(item)).collect(Collectors.toList());
    }

    @Override
    public MessageDto create(MessageDto messageDto) {
        Message message = messageRepository.save(messageConverter.toEntity(messageDto));
        MessageDto newMessageDto = messageConverter.toDto(message);
        List<MessagePhoto> messagePhotos = new ArrayList<>();
        for(MessagePhotoDto messagePhotoDto: messageDto.getPhotos()) {
            MessagePhoto messagePhoto = messagePhotoConverter.toEntity(messagePhotoDto);
            messagePhoto.setMessage(message);
            messagePhotos.add(messagePhotoRepository.save(messagePhoto));
        }
        newMessageDto.setPhotos(messagePhotos.stream().map(item -> messagePhotoConverter.toDto(item)).collect(Collectors.toList()));

        Room room = roomRepository.getById(message.getRoom().getId());
        for(User user: room.getUsers())
            messagingTemplate.convertAndSend("/topic/" + user.getId(), newMessageDto);
        room.setUpdatedAt(new Date());
        roomRepository.save(room);

        return newMessageDto;
    }
}
