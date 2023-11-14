package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.EAudience;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUserId(Integer userId, Pageable pageable);

    Page<Post> findAllByUserIdAndAudience(Integer userId, EAudience audience, Pageable pageable);

    @Query("SELECT post FROM Post post " +
            "WHERE (post.audience = :audience OR post.user.id = :userId) " +
            "AND post.content LIKE %:content% ")
    Page<Post> findAllByContentContainsAndAudienceOrUserId(@Param("content") String content, @Param("audience") EAudience audience, @Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT post FROM Post post " +
            "WHERE (post.audience IN (:audiences) OR post.user.id = :currentUserId) " +
            "AND (:userId IS NULL OR post.user.id = :userId) " +
            "AND (:content IS NULL OR :content = '' OR post.content LIKE %:content% ) " +
            "AND (:fromDate IS NULL OR :toDate IS NULL OR post.createdAt BETWEEN :fromDate AND :toDate) ")
    Page<Post> filter(@Param("content") String content, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("audiences") EAudience[] audiences, @Param("userId") Integer userId, @Param("currentUserId") Integer currentUserId, Pageable pageable);

    @Query("SELECT post FROM Post post LEFT JOIN post.comments comments " +
            "WHERE (post.audience IN (:audiences) OR post.user.id = :currentUserId) " +
            "AND (:userId IS NULL OR post.user.id = :userId) " +
            "AND (:content IS NULL OR :content = '' OR post.content LIKE %:content% ) " +
            "AND (:fromDate IS NULL OR :toDate IS NULL OR post.createdAt BETWEEN :fromDate AND :toDate) " +
            "GROUP BY post.id ORDER BY COUNT(comments) DESC")
    Page<Post> filterByTopComment(@Param("content") String content, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("audiences") EAudience[] audiences, @Param("userId") Integer userId, @Param("currentUserId") Integer currentUserId, Pageable pageable);

    @Query("SELECT post FROM Post post LEFT JOIN post.postReacts reacts " +
            "WHERE (post.audience IN (:audiences) OR post.user.id = :currentUserId) " +
            "AND (:userId IS NULL OR post.user.id = :userId) " +
            "AND (:content IS NULL OR :content = '' OR post.content LIKE %:content% ) " +
            "AND (:fromDate IS NULL OR :toDate IS NULL OR post.createdAt BETWEEN :fromDate AND :toDate) " +
            "GROUP BY post.id ORDER BY COUNT(reacts) DESC")
    Page<Post> filterByTopReact(@Param("content") String content, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("audiences") EAudience[] audiences, @Param("userId") Integer userId, @Param("currentUserId") Integer currentUserId, Pageable pageable);
}
