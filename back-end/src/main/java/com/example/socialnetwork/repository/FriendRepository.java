package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    Page<Friend> findByReceiverUsernameContains(String username, Pageable pageable);
}
