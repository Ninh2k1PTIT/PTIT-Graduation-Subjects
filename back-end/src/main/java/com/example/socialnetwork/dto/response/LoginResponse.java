package com.example.socialnetwork.dto.response;

import com.example.socialnetwork.model.EGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatar;
    private EGender gender;
}
