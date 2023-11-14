package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.CommentReactDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.service.CommentReactService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class CommentReactController {
    private CommentReactService commentReactService;

    public CommentReactController(CommentReactService commentReactService) {
        this.commentReactService = commentReactService;
    }

    @PostMapping("/comment/{id}/react")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateByCommentId(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(commentReactService.updateByCommentId(id), true, null, null));
    }

    @GetMapping("comment/{id}/reacts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getByCommentId(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(new BaseResponse<>(commentReactService.getByCommentId(id, pageable), true, null, null));
    }
}
