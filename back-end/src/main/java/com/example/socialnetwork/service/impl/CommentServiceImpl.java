package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.CommentConverter;
import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.Comment;
import com.example.socialnetwork.repository.CommentRepository;
import com.example.socialnetwork.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private CommentConverter commentConverter;

    @Override
    public PaginationResponse<CommentDto> getAllByPostId(Integer postId, Pageable pageable) {
        Page<Comment> page = commentRepository.findAllByPostId(postId, pageable);
        PaginationResponse<CommentDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> commentConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setCurrentPage(page.getNumber());
        return result;
    }

    @Override
    public CommentDto getById(Integer id) {
        return commentConverter.toDto(commentRepository.findById(id).get());
    }

    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = commentConverter.toEntity(commentDto);
        return commentConverter.toDto(commentRepository.save(comment));
    }

    @Override
    public Boolean delete(Integer id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
