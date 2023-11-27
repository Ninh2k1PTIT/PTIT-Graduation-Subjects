package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.PostDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.dto.response.PaginationResponse;
import com.example.socialnetwork.model.EPostSort;
import com.example.socialnetwork.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody PostDto post) throws IOException {
        return ResponseEntity.ok(postService.create2(post));
    }

    @PutMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@RequestBody PostDto postDto, @PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postService.update(id, postDto), true, null, null));
    }

    @DeleteMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postService.delete(id), true, null, null));
    }

    @GetMapping("posts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String content, @RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate, @RequestParam(required = false) EPostSort sort, @RequestParam(required = false) Integer userId) throws IOException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(new BaseResponse<>(postService.search(content, fromDate, toDate, sort, userId, pageable), true, null, null));
    }

    @PostMapping("tests")
    @PreAuthorize("hasRole('USER')")
    public String test(@RequestBody Test test) throws IOException {
        String base64Image = test.test.split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        System.out.println(img.getHeight());
        return test.test;
    }

    @GetMapping("post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(postService.getById(id), true, null, null));
    }

//    @GetMapping("posts/self")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> getByCurrentUser(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String content, @RequestParam(required = false) EPostSort sort) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        return ResponseEntity.ok(new BaseResponse<>(postService.search(content, sort, userDetails.getId(), pageable), true, null, null));
//    }

    @GetMapping("friend/{id}/posts")
    @PreAuthorize("hasRole('USER')")
    public PaginationResponse<PostDto> getFriendPosts(@RequestParam Integer page, @RequestParam Integer size, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getAllByUserId(id, pageable);
    }
}

class Test {
    public String test;
}