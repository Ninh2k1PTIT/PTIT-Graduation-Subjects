package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Friend> friends = user.getFriends1();
        friends.addAll(user.getFriends2());
        if(friends.stream().anyMatch(item -> item.getSender().getId() == userDetails.getId() || item.getReceiver().getId() == userDetails.getId()))
            userDto.setIsFriend(true);
        else
            userDto.setIsFriend(false);

        return userDto;
    }
}
