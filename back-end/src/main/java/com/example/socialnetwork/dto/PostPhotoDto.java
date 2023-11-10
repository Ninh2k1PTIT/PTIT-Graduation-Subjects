package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostPhotoDto {
    private Integer id;
    private String content;
    private PostDto post;
}