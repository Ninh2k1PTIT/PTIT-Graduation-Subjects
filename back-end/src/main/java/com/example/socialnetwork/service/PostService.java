package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.EPostSort;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostDto create(PostDto postDto, MultipartFile[] files);
    PostDto update(Integer id, PostDto postDto);
    PaginationResponse<PostDto> getAll(String content, EPostSort sort, Pageable pageable);
    PaginationResponse<PostDto> getAllByUserId(Integer userId, Pageable pageable);
    PostDto getById(Integer userId);
    Boolean delete(Integer id);
}
