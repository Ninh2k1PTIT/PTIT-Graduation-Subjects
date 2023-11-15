package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendDto {
    public Integer id;
    public UserDto sender;
    public UserDto receiver;
    public Boolean isFriend;
}
