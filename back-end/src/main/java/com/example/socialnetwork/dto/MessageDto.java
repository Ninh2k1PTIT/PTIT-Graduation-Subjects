package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private String content;
    private Date createdAt;
    private Integer userId;
    private Integer roomId;
    private List<MessagePhotoDto> photos;
}
