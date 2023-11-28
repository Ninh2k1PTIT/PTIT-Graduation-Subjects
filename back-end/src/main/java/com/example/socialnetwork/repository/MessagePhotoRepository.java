package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.MessagePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagePhotoRepository extends JpaRepository<MessagePhoto, Integer> {
}
