package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostPhotoDto {
    private Integer id;
    private String name;
    private String content;
    private String type;
    private Integer postId;
    private List<TagDto> tags;
}
