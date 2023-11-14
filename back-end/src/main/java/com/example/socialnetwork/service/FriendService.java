package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.FriendDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface FriendService {
    FriendDto create(FriendDto friendDto);

    PaginationResponse<FriendDto> search(String name, Pageable pageable);
}
