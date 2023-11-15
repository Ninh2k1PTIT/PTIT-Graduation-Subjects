package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    Page<Friend> findByReceiverIdAndSenderUsernameContainsAndAcceptedAtIsNull(Integer id, String username, Pageable pageable);

    @Query("SELECT friend FROM Friend friend WHERE " +
            "(friend.receiver.id = :loggedInUserId OR friend.sender.id = :loggedInUserId) " +
            "AND (:username IS NULL OR friend.sender.username LIKE %:username% OR friend.receiver.username LIKE %:username%) " +
            "AND (friend.acceptedAt IS NOT NULL)")
    Page<Friend> searchFriend(@Param("loggedInUserId") Integer loggedInUserId, @Param("username") String username, Pageable pageable);
}
