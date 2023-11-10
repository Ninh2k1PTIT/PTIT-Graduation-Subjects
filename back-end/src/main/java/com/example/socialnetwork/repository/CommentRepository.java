package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByPostId(Integer postId, Pageable pageable);
    Page<Comment> findAllByPostIdAndIdLessThan(Integer postId, Integer lastCommentId, Pageable pageable);
}
