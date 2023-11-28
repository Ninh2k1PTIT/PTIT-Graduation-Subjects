package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.PostConverter;
import com.example.socialnetwork.converter.PostPhotoConverter;
import com.example.socialnetwork.converter.TagConverter;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostPhotoDto;
import com.example.socialnetwork.dto.TagDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.*;
import com.example.socialnetwork.repository.PostPhotoRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.TagRepository;
import com.example.socialnetwork.service.FirebaseImageService;
import com.example.socialnetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private PostPhotoRepository postPhotoRepository;
    private TagRepository tagRepository;
    private PostConverter postConverter;
    private PostPhotoConverter postPhotoConverter;
    private TagConverter tagConverter;
    private FirebaseImageService imageService;

    @Override
    public PostDto create(PostDto postDto) throws IOException {
        Post post = postConverter.toEntity(postDto);
        Post newPost = postRepository.save(post);
        PostDto newPostDto = postConverter.toDto(newPost);

        List<PostPhotoDto> postPhotoDtos = new ArrayList<>();
        for (PostPhotoDto postPhotoDto : postDto.getPhotos()) {
            String content = postPhotoDto.getContent().split(",")[1];
            byte[] bytes = DatatypeConverter.parseBase64Binary(content);
            String fileName = imageService.save(bytes, postPhotoDto.getName(), postPhotoDto.getType());
            String imageUrl = imageService.getImageUrl(fileName);

            PostPhoto postPhoto = new PostPhoto();
            postPhoto.setContent(imageUrl);
            postPhoto.setPost(newPost);
            PostPhoto newPostPhoto = postPhotoRepository.save(postPhoto);
            PostPhotoDto newPostPhotoDto = postPhotoConverter.toDto(newPostPhoto);

            List<TagDto> tagDtos = new ArrayList<>();
            for (TagDto tagDto : postPhotoDto.getTags()) {
                Tag tag = tagConverter.toEntity(tagDto);
                tag.setPostPhoto(newPostPhoto);
                tagDtos.add(tagConverter.toDto(tagRepository.save(tag)));
            }
            newPostPhotoDto.setTags(tagDtos);

            postPhotoDtos.add(newPostPhotoDto);
        }
        newPostDto.setPhotos(postPhotoDtos);

        return newPostDto;
    }

    @Override
    public PostDto update(Integer id, PostDto postDto) {
        postDto.setId(id);
        Post post = postRepository.findById(id).get();
        post.getPostPhotos().removeIf(item -> {
            for (PostPhotoDto postPhotoDto : postDto.getPhotos()) {
                if (postPhotoDto.getId() == item.getId())
                    return false;
            }
            return true;
        });
        post.setContent(postDto.getContent());
        post.setAudience(postDto.getAudience());

        //Save photo
        for (PostPhotoDto postPhotoDto : postDto.getPhotos()) {
            if (postPhotoDto.getId() == null) {
//                postPhotoDto.setPost(postDto);
                PostPhoto postPhoto = postPhotoConverter.toEntity(postPhotoDto);
                post.getPostPhotos().add(postPhoto);
            }
        }
        return postConverter.toDto(postRepository.save(post));
    }

    @Override
    public PaginationResponse<PostDto> getAllByUserId(Integer userId, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Page<Post> page;
        if (userDetails.getId() == userId)
            page = postRepository.findAllByUserId(userId, pageable);
        else
            page = postRepository.findAllByUserIdAndAudience(userId, EAudience.PUBLIC, pageable);

        PaginationResponse<PostDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> postConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());

        return result;
    }

    @Override
    public PaginationResponse<PostDto> search(String content, Long fromDateNumber, Long toDateNumber, EPostSort sort, Integer userId, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        EAudience[] audiences = new EAudience[]{EAudience.PUBLIC};
        Page<Post> page;
        Date fromDate = null, toDate = null;

        if (Objects.nonNull(fromDateNumber))
            fromDate = new Date(fromDateNumber);
        if (Objects.nonNull(toDateNumber))
            toDate = new Date(toDateNumber);

        if (sort == EPostSort.COMMENT)
            page = postRepository.filterByTopComment(content, fromDate, toDate, audiences, userId, userDetails.getId(), pageable);
        else if (sort == EPostSort.REACT)
            page = postRepository.filterByTopReact(content, fromDate, toDate, audiences, userId, userDetails.getId(), pageable);
        else page = postRepository.filter(content, fromDate, toDate, audiences, userId, userDetails.getId(), pageable);

        PaginationResponse<PostDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> postConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());

        return result;
    }

    @Override
    public PostDto getById(Integer id) {
        return postConverter.toDto(postRepository.findById(id).get());
    }

    @Override
    public Boolean delete(Integer id) {
        Po
//        if (postRepository.existsById(id)) {
//            postRepository.deleteById(id);
//            return true;
//        }
        return false;
    }
}
