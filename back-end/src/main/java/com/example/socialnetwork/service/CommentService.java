package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface CommentService {
    PaginationResponse<CommentDto> getAllByPostId(Integer postId, Pageable pageable);

    PaginationResponse<CommentDto> getByPostIdAndLastCommentId(Integer postId, Integer lastCommentId, Integer size);

    CommentDto getById(Integer id);

    CommentDto create(CommentDto commentDto);

    Boolean delete(Integer id);
}
