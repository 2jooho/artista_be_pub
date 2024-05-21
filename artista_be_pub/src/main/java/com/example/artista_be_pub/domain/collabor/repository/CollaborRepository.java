package com.artista.main.domain.collabor.repository;

import com.artista.main.domain.collabor.entity.CollaborEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaborRepository extends JpaRepository<CollaborEntity, String> {
    Page<CollaborEntity> findAll(Pageable pageable);
}
