package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.UserConverter;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.repository.UserRepository;
import com.example.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserConverter userConverter;

    @Override
    public UserDto updateAvatar(String avatar) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getEmail()).get();
        user.setAvatar(avatar);
        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateBasicInfo(UserDto userDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getEmail()).get();
        user.setUsername(userDto.getUsername());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setGender(userDto.getGender());
        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Integer id) {
        return userConverter.toDto(userRepository.findById(id).get());
    }

    @Override
    public UserDto getCurrent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userConverter.toDto(userRepository.findById(userDetails.getId()).get());
    }

    @Override
    public PaginationResponse<UserDto> search(String username, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Page<User> page = userRepository.findByUsernameContainsAndIdNot(username, userDetails.getId(), pageable);
        PaginationResponse<UserDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> userConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());
        return result;
    }

    @Override
    public List<UserDto> getByUsername(String username) {
        return userRepository.findByUsernameContains(username).stream().map(item -> userConverter.toDto(item)).collect(Collectors.toList());
    }
}
