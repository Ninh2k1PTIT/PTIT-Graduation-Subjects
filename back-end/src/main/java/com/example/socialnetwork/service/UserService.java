package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.UserDto;

public interface UserService {
    UserDto updateAvatar(String avatar);

    UserDto updateBasicInfo(UserDto userDto);

    UserDto getById(Integer id);
    UserDto getCurrent();
}
