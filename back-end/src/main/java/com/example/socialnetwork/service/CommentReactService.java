package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.CommentReactDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface CommentReactService {
    boolean updateByCommentId(Integer id);

    PaginationResponse<CommentReactDto> getByCommentId(Integer commentId, Pageable pageable);
}
