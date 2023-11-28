package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessagePhotoDto {
    private Integer id;
    private String content;
    private Integer messageId;
}
