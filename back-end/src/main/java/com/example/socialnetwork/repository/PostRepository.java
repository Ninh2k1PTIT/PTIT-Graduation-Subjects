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

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUserId(Integer userId, Pageable pageable);

    Page<Post> findAllByUserIdAndAudience(Integer userId, EAudience audience, Pageable pageable);

    @Query("SELECT post FROM Post post " +
            "WHERE (post.audience = :audience OR post.user.id = :userId) " +
            "AND post.content LIKE %:content% ")
    Page<Post> findAllByContentContainsAndAudienceOrUserId(@Param("content") String content, @Param("audience") EAudience audience, @Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT post FROM Post post LEFT JOIN post.comments comments " +
            "WHERE (post.audience = :audience OR post.user.id = :userId) " +
            "AND post.content LIKE %:content% " +
            "GROUP BY post.id ORDER BY COUNT(comments) DESC")
    Page<Post> findAllByTopComment(@Param("audience") EAudience audience,
                                   @Param("userId") Integer userId,
                                   @Param("content") String content, Pageable pageable);

    @Query("SELECT post FROM Post post LEFT JOIN post.postReacts reacts " +
            "WHERE (post.audience = :audience OR post.user.id = :userId) " +
            "AND post.content LIKE %:content% " +
            "GROUP BY post.id ORDER BY COUNT(reacts) DESC")
    Page<Post> findAllByTopReact(@Param("audience") EAudience audience,
                                 @Param("userId") Integer userId,
                                 @Param("content") String content, Pageable pageable);
}
