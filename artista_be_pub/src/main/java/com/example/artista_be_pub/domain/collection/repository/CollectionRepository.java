package com.artista.main.domain.collection.repository;

import com.artista.main.domain.collection.entity.CollectionEntity;
import com.artista.main.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<CollectionEntity, String> {
    Optional<CollectionEntity> findById(Long id);
}
