package com.example.socialnetwork.dto;

import com.example.socialnetwork.model.EGender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String avatar;
    private String phoneNumber;
    private EGender gender;
    private String email;
    private Boolean isFriend;
}
