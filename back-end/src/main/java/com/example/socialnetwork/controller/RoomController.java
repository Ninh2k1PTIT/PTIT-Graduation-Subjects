package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.RoomDto;
import com.example.socialnetwork.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService groupService) {
        this.roomService = groupService;
    }

    @GetMapping("/rooms")
    public List<RoomDto> getAll() {
        return roomService.getAll();
    }

    @PostMapping("/room")
    public RoomDto create(@RequestBody RoomDto roomDto) {
        return roomService.create(roomDto);
    }
}
