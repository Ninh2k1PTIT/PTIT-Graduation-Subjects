package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.PhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.model.Photo;
import com.example.socialnetwork.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PhotoConverter {
    public Photo toEntity(PhotoDto photoDto) {
        Photo photo = new Photo();
        photo.setContent(photoDto.getContent());
        photo.setId(photoDto.getId());

        Post post = new Post();
        post.setId(photoDto.getPost().getId());
        photo.setPost(post);

        return photo;
    }

    public PhotoDto toDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setContent(photo.getContent());
        photoDto.setId(photo.getId());

        PostDto postDto = new PostDto();
        postDto.setId(photo.getId());
        photoDto.setPost(postDto);

        return photoDto;
    }
}
