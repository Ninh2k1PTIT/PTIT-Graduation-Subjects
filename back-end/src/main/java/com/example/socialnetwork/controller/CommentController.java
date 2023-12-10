package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDto> create(@RequestBody CommentDto commentDto)  {
        return ResponseEntity.ok(commentService.create(commentDto));
    }

    @GetMapping("post/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByPostId(@RequestParam(required = false) Integer lastCommentId, @RequestParam Integer size, @PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(commentService.getByPostIdAndLastCommentId(id, lastCommentId, size), true, null, null));
    }

    @DeleteMapping("comment/{id}")
    @PreAuthorize("hasRole('USER')")
    public Boolean delete(@PathVariable Integer id) {
        return commentService.delete(id);
    }

    @GetMapping("comment/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(commentService.getById(id), true, null, null));
    }
}
