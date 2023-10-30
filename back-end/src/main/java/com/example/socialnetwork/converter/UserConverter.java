package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setAvatar(user.getAvatar());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setGender(user.getGender());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
