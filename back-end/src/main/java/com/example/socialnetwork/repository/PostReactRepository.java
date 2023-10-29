package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.PostReact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactRepository extends JpaRepository<PostReact, Integer> {
    PostReact findByPostIdAndUserId(Integer postId, Integer userId);

    Page<PostReact> findAllByPostId(Integer postId, Pageable pageable);
}
