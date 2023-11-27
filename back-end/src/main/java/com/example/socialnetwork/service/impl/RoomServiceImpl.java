package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.RoomConverter;
import com.example.socialnetwork.dto.RoomDto;
import com.example.socialnetwork.model.Room;
import com.example.socialnetwork.repository.RoomRepository;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private RoomConverter roomConverter;
    private UserRepository userRepository;

    @Override
    public List<RoomDto> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return roomRepository.findByUsers_Id(userDetails.getId()).stream().map(item -> roomConverter.toDto(item)).collect(Collectors.toList());
    }

    @Override
    public RoomDto create(RoomDto roomDto) {
        Room room = new Room();
        room.setUsers(roomDto.getUsers().stream().map(item -> userRepository.getById(item.getId())).collect(Collectors.toSet()));
        return roomConverter.toDto(roomRepository.save(room));
    }
}
