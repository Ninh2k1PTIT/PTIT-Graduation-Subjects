package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.ERelationship;
import com.example.socialnetwork.dto.FriendDto;
import com.example.socialnetwork.dto.response.BaseResponse;
import com.example.socialnetwork.service.FriendService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class FriendController {
    private FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("friends")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false, defaultValue = "") String username, @RequestParam ERelationship relationship) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(new BaseResponse<>(friendService.search(username, relationship, pageable), true, null, null));
    }

    @PostMapping("friend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody FriendDto friendDto) {
        return ResponseEntity.ok(new BaseResponse<>(friendService.create(friendDto), true, null, null));
    }

    @PutMapping("friend/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(friendService.updateById(id), true, null, null));
    }

    @DeleteMapping("friend/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(new BaseResponse<>(friendService.deleteById(id), true, null, null));
    }
}
