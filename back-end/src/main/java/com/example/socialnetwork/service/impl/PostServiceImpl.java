package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.PhotoConverter;
import com.example.socialnetwork.converter.PostConverter;
import com.example.socialnetwork.dto.PhotoDto;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.EAudience;
import com.example.socialnetwork.model.EPostSort;
import com.example.socialnetwork.model.Photo;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.repository.PhotoRepository;
import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.service.FirebaseImageService;
import com.example.socialnetwork.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private PhotoRepository photoRepository;
    private PostConverter postConverter;
    private PhotoConverter photoConverter;
    private FirebaseImageService imageService;


//    @Override
//    public PostDto create(PostDto postDto) {
//        //Save post
//        Post post = postConverter.toEntity(postDto);
//        Post newPost = postRepository.save(post);
//        PostDto result = postConverter.toDto(newPost);
//
//        //Save photo
//        List<PhotoDto> photoDtos = new ArrayList<>();
//        for (PhotoDto photoDto : postDto.getPhotos()) {
//            photoDto.setPost(result);
//            Photo photo = photoConverter.toEntity(photoDto);
//            Photo newPhoto = photoRepository.save(photo);
//            photoDtos.add(photoConverter.toDto(newPhoto));
//        }
//        result.setPhotos(photoDtos);
//
//        return result;
//    }

    @Override
    public PostDto create(PostDto postDto, MultipartFile[] files) {
        //Save post
        Post post = postConverter.toEntity(postDto);
        Post newPost = postRepository.save(post);
        PostDto result = postConverter.toDto(newPost);

        //Save photo
        List<PhotoDto> photoDtos = new ArrayList<>();
        for(MultipartFile file: files) {
            try {
                String fileName = imageService.save(file);
                String imageUrl = imageService.getImageUrl(fileName);
                Photo photo = new Photo();
                photo.setContent(imageUrl);
                photo.setPost(newPost);
                Photo newPhoto = photoRepository.save(photo);
                photoDtos.add(photoConverter.toDto(newPhoto));
            } catch (Exception e) {}
        }
        result.setPhotos(photoDtos);

        return result;
    }

    @Override
    public PostDto update(Integer id, PostDto postDto) {
        postDto.setId(id);
        Post post = postRepository.findById(id).get();
        post.getPhotos().removeIf(item -> {
            for (PhotoDto photoDto : postDto.getPhotos()) {
                if (photoDto.getId() == item.getId())
                    return false;
            }
            return true;
        });
        post.setContent(postDto.getContent());
        post.setAudience(postDto.getAudience());

        //Save photo
        for (PhotoDto photoDto : postDto.getPhotos()) {
            if (photoDto.getId() == null) {
                photoDto.setPost(postDto);
                Photo photo = photoConverter.toEntity(photoDto);
                post.getPhotos().add(photo);
            }
        }
        return postConverter.toDto(postRepository.save(post));
    }

    @Override
    public PaginationResponse<PostDto> getAll(String content, EPostSort sort, Pageable pageable) {
        if (content == null)
            content = "";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Page<Post> page;
        System.out.println(content);
        if (sort == EPostSort.COMMENT)
            page = postRepository.findAllByTopComment(EAudience.PUBLIC, userDetails.getId(), content, pageable);
        else if (sort == EPostSort.REACT)
            page = postRepository.findAllByTopReact(EAudience.PUBLIC, userDetails.getId(), content, pageable);
        else
            page = postRepository.findAllByContentContainsAndAudienceOrUserId(content, EAudience.PUBLIC, userDetails.getId(), pageable);
        PaginationResponse<PostDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> postConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());

        return result;
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
    public PostDto getById(Integer id) {
        return postConverter.toDto(postRepository.findById(id).get());
    }

    @Override
    public Boolean delete(Integer id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
