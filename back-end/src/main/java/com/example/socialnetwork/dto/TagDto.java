package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagDto {
    private Integer id;
    private Integer offsetLeft;
    private Integer offsetTop;
    private Integer postPhotoId;
    private UserDto user;
}
