package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.RoomDto;

import java.util.List;

public interface RoomService {
    List<RoomDto> getAll();
    RoomDto create(RoomDto groupDto);
}
