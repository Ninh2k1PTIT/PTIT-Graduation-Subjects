package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.service.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("post/{id}/comment")
    @PreAuthorize("hasRole('USER')")
    public CommentDto create(@PathVariable Integer id, @RequestBody CommentDto commentDto) {
        PostDto postDto = new PostDto();
        postDto.setId(id);
        commentDto.setPost(postDto);
        return commentService.create(commentDto);
    }

    @GetMapping("post/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<CommentDto> getByPost(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentService.getAllByPostId(id, pageable);
    }

    @DeleteMapping("comment/{id}")
    @PreAuthorize("hasRole('USER')")
    public Boolean delete(@PathVariable Integer id) {
        return commentService.delete(id);
    }

    @GetMapping("comment/{id}")
    @PreAuthorize("hasRole('USER')")
    public CommentDto getById(@PathVariable Integer id) {
        return commentService.getById(id);
    }
}
