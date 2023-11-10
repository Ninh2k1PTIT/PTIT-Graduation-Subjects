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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostReactServiceImpl implements PostReactService {
    private PostReactRepository postReactRepository;
    private PostReactConverter postReactConverter;

    @Override
    public Boolean updateByPostId(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        PostReact postReact = postReactRepository.findByPostIdAndUserId(id, userDetails.getId());
        if (postReact != null)
            postReactRepository.delete(postReact);
        else {
            postReact = new PostReact();
            Post post = new Post();
            post.setId(id);
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
