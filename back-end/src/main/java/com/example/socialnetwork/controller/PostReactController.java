package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.UserDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.service.PostReactService;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> updateByPost(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postReactService.updateByPostId(id), true, null, null));
    }

    @GetMapping("post/{id}/reacts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostReactDto> getByPostId(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postReactService.getByPostId(id, pageable);
    }
}
