package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.FriendConverter;
import com.example.socialnetwork.dto.FriendDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {
    private FriendRepository friendRepository;
    private FriendConverter friendConverter;

    @Override
    public FriendDto create(FriendDto friendDto) {
        Friend friend = friendConverter.toEntity(friendDto);
        return friendConverter.toDto(friendRepository.save(friend));
    }

    @Override
    public PaginationResponse<FriendDto> search(String username, Pageable pageable) {
        Page<Friend> page = friendRepository.findByReceiverUsernameContains(username, pageable);
        PaginationResponse<FriendDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> friendConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());
        return result;
    }
}
