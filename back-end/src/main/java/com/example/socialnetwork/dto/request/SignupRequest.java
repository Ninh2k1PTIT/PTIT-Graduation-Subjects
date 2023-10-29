package com.example.socialnetwork.dto.request;

import com.example.socialnetwork.model.EGender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private EGender gender;
    private Set<String> role;
}
