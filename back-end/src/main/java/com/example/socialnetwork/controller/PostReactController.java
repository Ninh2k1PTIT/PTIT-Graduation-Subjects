package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.service.PostReactService;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class PostReactController {
    private PostReactService postReactService;

    public PostReactController(PostReactService postReactService) {
        this.postReactService = postReactService;
    }

    @PostMapping("post/{id}/react")
    @PreAuthorize("hasRole('USER')")
    public boolean updateByPost(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        PostDto postDto = new PostDto();
        postDto.setId(id);

        UserDto userDto = new UserDto();
        userDto.setId(userDetails.getId());
        postDto.setCreatedBy(userDto);

        return postReactService.updateByPost(postDto);
    }

    @GetMapping("post/{id}/reacts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostReactDto> getByPostId(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postReactService.getByPostId(id, pageable);
    }
}
