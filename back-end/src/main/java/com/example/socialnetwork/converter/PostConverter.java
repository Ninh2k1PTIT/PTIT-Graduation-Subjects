package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.PhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.Photo;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter {
    public Post toEntity(PostDto postDto) {
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setAudience(postDto.getAudience());

        return post;
    }

    public PostDto toDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setAudience(post.getAudience());
        postDto.setTotalReact(post.getPostReacts().size());
        postDto.setTotalComment(post.getComments().size());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());

        UserDto userDto = new UserDto();
        userDto.setId(post.getUser().getId());
        userDto.setUsername(post.getUser().getUsername());
        userDto.setAvatar(post.getUser().getAvatar());
        postDto.setUser(userDto);

        List<PhotoDto> photoDtos = new ArrayList<>();
        for (Photo photo : post.getPhotos()) {
            PhotoDto photoDto = new PhotoDto();
            photoDto.setId(photo.getId());
            photoDto.setContent(photo.getContent());
            photoDtos.add(photoDto);
        }
        postDto.setPhotos(photoDtos);

        postDto.setIsReact(false);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        if (post.getPostReacts().stream().anyMatch(item -> item.getUser().getId() == userDetails.getId()))
            postDto.setIsReact(true);

        return postDto;
    }
}
