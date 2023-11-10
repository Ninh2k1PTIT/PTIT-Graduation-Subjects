package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.CommentPhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.model.Comment;
import com.example.socialnetwork.model.CommentPhoto;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverter {
    public Comment toEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());

        Post post = new Post();
        post.setId(commentDto.getPostId());
        comment.setPost(post);

        return comment;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setTotalReact(comment.getCommentReacts().size());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setUpdatedAt(comment.getUpdatedAt());
        commentDto.setPostId(comment.getPost().getId());

        UserDto userDto = new UserDto();
        userDto.setId(comment.getUser().getId());
        userDto.setUsername(comment.getUser().getUsername());
        userDto.setAvatar(comment.getUser().getAvatar());
        commentDto.setCreatedBy(userDto);

        List<CommentPhotoDto> commentPhotoDtos = new ArrayList<>();
        for(CommentPhoto commentPhoto: comment.getCommentPhotos()) {
            CommentPhotoDto commentPhotoDto = new CommentPhotoDto();
            commentPhotoDto.setId(commentPhoto.getId());
            commentPhotoDto.setContent(commentPhoto.getContent());
            commentPhotoDtos.add(commentPhotoDto);
        }
        commentDto.setPhotos(commentPhotoDtos);

        commentDto.setIsReact(false);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        if (comment.getCommentReacts().stream().anyMatch(item -> item.getUser().getId() == userDetails.getId()))
            commentDto.setIsReact(true);

        return commentDto;
    }
}
