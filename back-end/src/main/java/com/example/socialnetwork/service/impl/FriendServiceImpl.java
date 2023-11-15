package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.FriendConverter;
import com.example.socialnetwork.dto.ERelationship;
import com.example.socialnetwork.dto.FriendDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.exception.EntityNotFoundException;
import com.example.socialnetwork.exception.NotAllowedException;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
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
    public FriendDto updateById(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Friend friend = friendRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lời mời"));
        if (friend.getReceiver().getId() == userDetails.getId()) {
            friend.setAcceptedAt(new Date());
            return friendConverter.toDto(friendRepository.save(friend));
        } else throw new NotAllowedException("Không có quyền");
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (friendRepository.existsById(id)) {
            friendRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public PaginationResponse<FriendDto> search(String username, ERelationship relationship, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Page<Friend> page;
        if (relationship == ERelationship.FRIEND)
            page = friendRepository.searchFriend(userDetails.getId(), username, pageable);
        else
            page = friendRepository.findByReceiverIdAndSenderUsernameContainsAndAcceptedAtIsNull(userDetails.getId(), username, pageable);

        PaginationResponse<FriendDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> friendConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());
        return result;
    }
}
