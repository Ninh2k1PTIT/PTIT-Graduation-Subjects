package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin("*")
public class UserController {
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("updateAvatar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> updateAvatar(@RequestParam(name = "file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateAvatar(file));
    }

    @PutMapping("updateInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> updateBasicInfo(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateBasicInfo(userDto));
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasRole('USER')")
    public UserDto getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping("user/self")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BaseResponse<UserDto>> getCurrent() {
        return ResponseEntity.ok(new BaseResponse<>(userService.getCurrent(), true, null, null));
    }

    @GetMapping("users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false, defaultValue = "") String username) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(userService.search(username, pageable), true, null, null));
    }

    @GetMapping("users/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByUsername(@RequestParam(defaultValue = "") String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }
}
