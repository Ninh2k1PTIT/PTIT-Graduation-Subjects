package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {
    private Integer id;
    private Set<UserDto> users;
    private MessageDto lastMessage;
}
