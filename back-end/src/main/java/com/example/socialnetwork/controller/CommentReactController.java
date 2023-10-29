package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.*;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.service.CommentReactService;
import com.example.socialnetwork.service.impl.UserDetailsImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public boolean updateByComment(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);

        UserDto userDto = new UserDto();
        userDto.setId(userDetails.getId());
        commentDto.setUser(userDto);

        return commentReactService.updateByComment(commentDto);
    }

    @GetMapping("comment/{id}/reacts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<CommentReactDto> getByCommentId(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentReactService.getByCommentId(id, pageable);
    }
}
