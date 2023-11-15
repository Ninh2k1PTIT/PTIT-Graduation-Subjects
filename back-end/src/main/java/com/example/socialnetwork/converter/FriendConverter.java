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

        UserDto sender = new UserDto();
        sender.setId(friend.getSender().getId());
        sender.setUsername(friend.getSender().getUsername());
        sender.setAvatar(friend.getSender().getAvatar());
        friendDto.setSender(sender);

        UserDto receiver = new UserDto();
        receiver.setId(friend.getReceiver().getId());
        receiver.setUsername(friend.getReceiver().getUsername());
        receiver.setAvatar(friend.getReceiver().getAvatar());
        friendDto.setReceiver(receiver);

        if (friend.getAcceptedAt() != null)
            friendDto.setIsFriend(true);
        else friendDto.setIsFriend(false);

        return friendDto;
    }
}
