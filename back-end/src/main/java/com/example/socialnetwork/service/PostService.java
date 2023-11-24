package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.EPostSort;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {
    PostDto create(PostDto postDto, MultipartFile[] files);
    PostDto create2(PostDto postDto) throws IOException;

    PostDto update(Integer id, PostDto postDto);

    PaginationResponse<PostDto> getAllByUserId(Integer userId, Pageable pageable);

    PostDto getById(Integer userId);

    Boolean delete(Integer id);

    PaginationResponse<PostDto> search(String content, Long fromDate, Long toDate, EPostSort sort, Integer userId, Pageable pageable);
}
