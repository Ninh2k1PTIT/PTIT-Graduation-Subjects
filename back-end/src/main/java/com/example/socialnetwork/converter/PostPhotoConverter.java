package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.PostPhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.model.PostPhoto;
import com.example.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostPhotoConverter {
    private TagConverter tagConverter;
    public PostPhoto toEntity(PostPhotoDto postPhotoDto) {
        PostPhoto postPhoto = new PostPhoto();
        postPhoto.setContent(postPhotoDto.getContent());
        postPhoto.setId(postPhotoDto.getId());

        Post post = new Post();
        post.setId(postPhotoDto.getId());
        postPhoto.setPost(post);

        return postPhoto;
    }

    public PostPhotoDto toDto(PostPhoto postPhoto) {
        PostPhotoDto postPhotoDto = new PostPhotoDto();
        postPhotoDto.setContent(postPhoto.getContent());
        postPhotoDto.setId(postPhoto.getId());
        postPhotoDto.setPostId(postPhoto.getPost().getId());
        postPhotoDto.setTags(postPhoto.getTags().stream().map(item -> tagConverter.toDto(item)).collect(Collectors.toList()));

        return postPhotoDto;
    }
}
