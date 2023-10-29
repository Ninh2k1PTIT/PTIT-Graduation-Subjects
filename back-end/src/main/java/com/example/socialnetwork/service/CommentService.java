package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    PaginationResponse<CommentDto> getAllByPostId(Integer userId, Pageable pageable);

    CommentDto getById(Integer id);
    CommentDto create(CommentDto commentDto);
    Boolean delete(Integer id);
}
