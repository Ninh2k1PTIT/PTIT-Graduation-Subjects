package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.PostPhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.model.PostPhoto;
import com.example.socialnetwork.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostPhotoConverter {
    public PostPhoto toEntity(PostPhotoDto postPhotoDto) {
        PostPhoto postPhoto = new PostPhoto();
        postPhoto.setContent(postPhotoDto.getContent());
        postPhoto.setId(postPhotoDto.getId());

        Post post = new Post();
        post.setId(postPhotoDto.getPost().getId());
        postPhoto.setPost(post);

        return postPhoto;
    }

    public PostPhotoDto toDto(PostPhoto postPhoto) {
        PostPhotoDto postPhotoDto = new PostPhotoDto();
        postPhotoDto.setContent(postPhoto.getContent());
        postPhotoDto.setId(postPhoto.getId());

        PostDto postDto = new PostDto();
        postDto.setId(postPhoto.getPost().getId());
        postPhotoDto.setPost(postDto);

        return postPhotoDto;
    }
}
