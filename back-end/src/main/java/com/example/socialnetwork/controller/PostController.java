package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.EPostSort;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//    @PostMapping("post")
//    @PreAuthorize("hasRole('USER')")
//    public PostDto create(@RequestBody PostDto postDto) {
//        return postService.create(postDto);
//    }

    @PostMapping("post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestParam(required = false) String post, @RequestParam(required = false) MultipartFile[] files) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PostDto postDto = mapper.readValue(post, PostDto.class);
        return ResponseEntity.ok(new BaseResponse<>(postService.create(postDto, files), true, null, null));
    }

    @PutMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@RequestBody PostDto postDto, @PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postService.update(id, postDto), true, null, null));
    }

    @DeleteMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postService.delete(id), true, null, null));
    }

    @GetMapping("posts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String content, @RequestParam(required = false) EPostSort sort, @RequestParam(required = false) Integer userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(new BaseResponse<>(postService.search(content, sort, userId, pageable), true, null, null));
    }

    @GetMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postService.getById(id), true, null, null));
    }

    @GetMapping("posts/self")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByCurrentUser(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String content, @RequestParam(required = false) EPostSort sort) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(new BaseResponse<>(postService.search(content, sort, userDetails.getId(), pageable), true, null, null));
    }

    @GetMapping("friend/{id}/posts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostDto> getFriendPosts(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getAllByUserId(id, pageable);
    }
}
