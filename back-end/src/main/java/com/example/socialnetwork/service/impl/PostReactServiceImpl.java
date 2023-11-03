package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.converter.PostReactConverter;
import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.PostReactDto;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.PostReact;
import com.example.socialnetwork.repository.PostReactRepository;
import com.example.socialnetwork.service.PostReactService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostReactServiceImpl implements PostReactService {
    private PostReactRepository postReactRepository;
    private PostReactConverter postReactConverter;

    @Override
    public boolean updateByPost(PostDto postDto) {
        PostReact postReact = postReactRepository.findByPostIdAndUserId(postDto.getId(), postDto.getCreatedBy().getId());
        if (postReact != null)
            postReactRepository.delete(postReact);
        else {
            postReact = new PostReact();
            Post post = new Post();
            post.setId(postDto.getId());
            postReact.setPost(post);
            postReactRepository.save(postReact);
        }
        return true;
    }

    @Override
    public PaginationResponse<PostReactDto> getByPostId(Integer postId, Pageable pageable) {
        Page<PostReact> page = postReactRepository.findAllByPostId(postId, pageable);

        PaginationResponse<PostReactDto> result = new PaginationResponse<>();
        result.setData(page.getContent().stream().map(item -> postReactConverter.toDto(item)).collect(Collectors.toList()));
        result.setTotalItems((int) page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());

        return result;
    }
}
