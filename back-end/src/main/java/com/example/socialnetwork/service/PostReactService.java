package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface PostReactService {
    boolean updateByPost(PostDto postDto);
    
    PaginationResponse<PostReactDto> getByPostId(Integer postId, Pageable pageable);
}
