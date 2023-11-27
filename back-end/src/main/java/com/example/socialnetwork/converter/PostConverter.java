package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostConverter {
    private PostPhotoConverter postPhotoConverter;
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
        postDto.setPhotos(post.getPostPhotos().stream().map(item -> postPhotoConverter.toDto(item)).collect(Collectors.toList()));
        postDto.setIsReact(false);

        UserDto userDto = new UserDto();
        userDto.setId(post.getUser().getId());
        userDto.setUsername(post.getUser().getUsername());
        userDto.setAvatar(post.getUser().getAvatar());
        postDto.setCreatedBy(userDto);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        if (post.getPostReacts().stream().anyMatch(item -> item.getUser().getId() == userDetails.getId()))
            postDto.setIsReact(true);

        return postDto;
    }
}
