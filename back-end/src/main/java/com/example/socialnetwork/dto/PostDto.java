package com.example.socialnetwork.dto;

import com.example.socialnetwork.model.EAudience;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Integer id;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private EAudience audience;
    private UserDto createdBy;
    private List<PostPhotoDto> photos;
    private Integer totalReact;
    private Integer totalComment;
    private Boolean isReact;
}
