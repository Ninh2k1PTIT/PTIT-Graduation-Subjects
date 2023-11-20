package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByRoomId(Integer roomId);
}
