package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.RoomDto;
import com.example.socialnetwork.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoomConverter {
    private UserConverter userConverter;
    private MessageConverter messageConverter;

    public Room toEntity(RoomDto roomDto) {
        Room group = new Room();
        return group;
    }

    public RoomDto toDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setUsers(room.getUsers().stream().map(item -> userConverter.toDto(item)).collect(Collectors.toSet()));
        if (!room.getMessages().isEmpty())
            roomDto.setLastMessage(messageConverter.toDto(room.getMessages().get(room.getMessages().size() - 1)));
        return roomDto;
    }
}
