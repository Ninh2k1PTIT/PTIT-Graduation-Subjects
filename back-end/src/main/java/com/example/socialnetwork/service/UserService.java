package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto updateAvatar(String avatar);

    UserDto updateBasicInfo(UserDto userDto);

    UserDto getById(Integer id);
    UserDto getCurrent();
    PaginationResponse<UserDto> search(String username, Pageable pageable);

    List<UserDto> getByUsername(String username);
}
