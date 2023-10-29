package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.EPostSort;
import com.example.socialnetwork.service.PostService;
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
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("post")
    @PreAuthorize("hasRole('USER')")
    public PostDto create(@RequestBody PostDto postDto) {
        return postService.create(postDto);
    }

    @PutMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public PostDto update(@RequestBody PostDto postDto, @PathVariable Integer id) {
        return postService.update(id, postDto);
    }

    @DeleteMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public Boolean delete(@PathVariable Integer id) {
        return postService.delete(id);
    }

    @GetMapping("posts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostDto> getAll(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String content, @RequestParam(required = false) EPostSort sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getAll(content, sort, pageable);
    }

    @GetMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public PostDto getById(@PathVariable Integer id) {
        return postService.getById(id);
    }

    @GetMapping("my-posts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostDto> getByCurrentUser(@RequestParam Integer page, @RequestParam Integer size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getAllByUserId(userDetails.getId(), pageable);
    }

    @GetMapping("friend/{id}/posts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostDto> getFriendPosts(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getAllByUserId(id, pageable);
    }
}
