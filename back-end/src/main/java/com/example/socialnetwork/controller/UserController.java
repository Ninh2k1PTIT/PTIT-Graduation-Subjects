package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("updateAvatar")
    @PreAuthorize("hasRole('USER')")
    public UserDto updateAvatar(@RequestBody UserDto userDto) {
        return userService.updateAvatar(userDto.getAvatar());
    }

    @PutMapping("updateBasicInfo")
    @PreAuthorize("hasRole('USER')")
    public UserDto updateBasicInfo(@RequestBody UserDto userDto) {
        return userService.updateBasicInfo(userDto);
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasRole('USER')")
    public UserDto getById(@PathVariable Integer id) {
        return userService.getById(id);
    }
}
