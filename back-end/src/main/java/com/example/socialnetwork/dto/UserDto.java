package com.example.socialnetwork.dto;

import com.example.socialnetwork.model.EGender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private ERelationship relationship;
    private String address;
    private String description;
    private Date birthday;
}

