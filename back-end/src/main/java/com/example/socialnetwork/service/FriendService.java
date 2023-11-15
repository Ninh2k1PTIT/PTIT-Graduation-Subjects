package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.ERelationship;
import com.example.socialnetwork.dto.FriendDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.exception.EntityNotFoundException;
import org.springframework.data.domain.Pageable;

public interface FriendService {
    FriendDto create(FriendDto friendDto);

    FriendDto updateById(Integer id) throws EntityNotFoundException;

    Boolean deleteById(Integer id);

    PaginationResponse<FriendDto> search(String name, ERelationship relationship, Pageable pageable);
}
