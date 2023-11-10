package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.CommentReactConverter;
import com.example.socialnetwork.dto.CommentReactDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.Comment;
import com.example.socialnetwork.model.CommentReact;
import com.example.socialnetwork.repository.CommentReactRepository;
import com.example.socialnetwork.service.CommentReactService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentReactServiceImpl implements CommentReactService {
    private CommentReactRepository commentReactRepository;
    private CommentReactConverter commentReactConverter;

    @Override
    public boolean updateByCommentId(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        CommentReact commentReact = commentReactRepository.findByCommentIdAndUserId(id, userDetails.getId());
        if (commentReact != null)
            commentReactRepository.delete(commentReact);
        else {
            commentReact = new CommentReact();
            Comment comment = new Comment();
            comment.setId(id);
            commentReact.setComment(comment);
            commentReactRepository.save(commentReact);
        }
        return true;
    }

    @Override
    public PaginationResponse<CommentReactDto> getByCommentId(Integer commentId, Pageable pageable) {
        Page<CommentReact> page = commentReactRepository.findAllByCommentId(commentId, pageable);

        PaginationResponse<CommentReactDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> commentReactConverter.toDto(item)).collect(Collectors.toList()));
        result.setCurrentPage(page.getNumber());
        result.setTotalItems((int) page.getTotalElements());
        result.setTotalPages(page.getTotalPages());

        return result;
    }
}
