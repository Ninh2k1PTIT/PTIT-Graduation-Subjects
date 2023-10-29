package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Integer id;
    private String content;
    private Integer totalReact;
    private Boolean isReact;
    private Date createdAt;
    private Date updatedAt;
    private UserDto user;
    private PostDto post;
}
