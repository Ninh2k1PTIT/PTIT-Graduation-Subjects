package com.example.socialnetwork.converter;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.CommentPhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostPhotoDto;
import com.example.socialnetwork.model.Comment;
import com.example.socialnetwork.model.CommentPhoto;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.PostPhoto;

public class CommentPhotoConverter {
    public CommentPhoto toEntity(CommentPhotoDto commentPhotoDto) {
        CommentPhoto commentPhoto = new CommentPhoto();
        commentPhoto.setContent(commentPhotoDto.getContent());
        commentPhoto.setId(commentPhotoDto.getId());

        Comment comment = new Comment();
        comment.setId(commentPhotoDto.getComment().getId());
        commentPhoto.setComment(comment);

        return commentPhoto;
    }

    public CommentPhotoDto toDto(CommentPhoto commentPhoto) {
        CommentPhotoDto commentPhotoDto = new CommentPhotoDto();
        commentPhotoDto.setContent(commentPhoto.getContent());
        commentPhotoDto.setId(commentPhoto.getId());

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentPhoto.getComment().getId());
        commentPhotoDto.setComment(commentDto);

        return commentPhotoDto;
    }
}
