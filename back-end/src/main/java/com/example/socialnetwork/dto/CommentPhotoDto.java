package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentPhotoDto {
    private Integer id;
    private String content;
    private String name;
    private String type;
    private Integer commentId;
}
