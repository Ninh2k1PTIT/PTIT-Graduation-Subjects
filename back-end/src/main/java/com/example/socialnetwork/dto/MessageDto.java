package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private String content;
    private Date createdAt;
    private Integer receiverUserId;
}
