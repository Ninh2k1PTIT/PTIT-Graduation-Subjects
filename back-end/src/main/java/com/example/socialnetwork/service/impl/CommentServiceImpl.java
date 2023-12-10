package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.CommentConverter;
import com.example.socialnetwork.converter.CommentPhotoConverter;
import com.example.socialnetwork.dto.CommentDto;
import com.example.socialnetwork.dto.CommentPhotoDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.Comment;
import com.example.socialnetwork.model.CommentPhoto;
import com.example.socialnetwork.repository.CommentPhotoRepository;
import com.example.socialnetwork.repository.CommentRepository;
import com.example.socialnetwork.service.CommentService;
import com.example.socialnetwork.service.FirebaseImageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private CommentPhotoRepository commentPhotoRepository;
    private CommentConverter commentConverter;
    private CommentPhotoConverter commentPhotoConverter;
    private FirebaseImageService imageService;

    @Override
    public PaginationResponse<CommentDto> getByPostIdAndLastCommentId(Integer postId, Integer lastCommentId, Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> page;
        if (lastCommentId == null) page = commentRepository.findAllByPostId(postId, pageable);
        else page = commentRepository.findAllByPostIdAndIdLessThan(postId, lastCommentId, pageable);
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
        Comment newComment = commentRepository.save(comment);
        CommentDto newCommentDto = commentConverter.toDto(newComment);

        List<CommentPhotoDto> commentPhotoDtos = new ArrayList<>();
        for (CommentPhotoDto commentPhotoDto : commentDto.getPhotos()) {
            String content = commentPhotoDto.getContent().split(",")[1];
            byte[] bytes = DatatypeConverter.parseBase64Binary(content);
            String fileName = imageService.save(bytes, commentPhotoDto.getName(), commentPhotoDto.getType());
            String imageUrl = imageService.getImageUrl(fileName);

            CommentPhoto commentPhoto = new CommentPhoto();
            commentPhoto.setContent(imageUrl);
            commentPhoto.setComment(newComment);
            CommentPhoto newCommentPhoto = commentPhotoRepository.save(commentPhoto);
            CommentPhotoDto newCommentPhotoDto = commentPhotoConverter.toDto(newCommentPhoto);
            commentPhotoDtos.add(newCommentPhotoDto);
        }
        newCommentDto.setPhotos(commentPhotoDtos);
        return newCommentDto;
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
