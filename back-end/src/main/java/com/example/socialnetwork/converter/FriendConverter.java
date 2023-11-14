package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.FriendDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

@Component
public class FriendConverter {
    public Friend toEntity(FriendDto friendDto) {
        Friend friend = new Friend();

        User user = new User();
        user.setId(friendDto.getReceiver().getId());
        friend.setReceiver(user);

        return friend;
    }

    public FriendDto toDto(Friend friend) {
        FriendDto friendDto = new FriendDto();
        friendDto.setId(friend.getId());

        UserDto userDto = new UserDto();
        userDto.setId(friend.getReceiver().getId());
        userDto.setUsername(friend.getReceiver().getUsername());
        userDto.setAvatar(friend.getReceiver().getAvatar());
        friendDto.setReceiver(userDto);

        return friendDto;
    }
}
