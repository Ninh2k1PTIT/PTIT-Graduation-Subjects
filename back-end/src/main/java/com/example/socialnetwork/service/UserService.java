package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto updateAvatar(MultipartFile file) throws IOException;

    UserDto updateBasicInfo(UserDto userDto);

    UserDto getById(Integer id);
    UserDto getCurrent();
    PaginationResponse<UserDto> search(String username, Pageable pageable);

    List<UserDto> getByUsername(String username);
}
