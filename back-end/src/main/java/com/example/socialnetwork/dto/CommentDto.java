package com.example.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
    private UserDto createdBy;
    private Integer postId;
    private List<CommentPhotoDto> photos;
}
