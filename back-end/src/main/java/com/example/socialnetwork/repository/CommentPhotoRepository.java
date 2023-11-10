package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.CommentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPhotoRepository extends JpaRepository<CommentPhoto, Integer> {
}
