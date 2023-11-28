package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByUsers_IdOrderByUpdatedAtDesc(Integer usersId);
}
