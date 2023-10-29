package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.CommentReact;
import com.example.socialnetwork.model.PostReact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReactRepository extends JpaRepository<CommentReact, Integer> {
    CommentReact findByCommentIdAndUserId(Integer commentId, Integer userId);

    Page<CommentReact> findAllByCommentId(Integer commentId, Pageable pageable);
}
