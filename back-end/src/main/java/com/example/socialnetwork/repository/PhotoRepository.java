package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<PostPhoto, Integer> {
}
