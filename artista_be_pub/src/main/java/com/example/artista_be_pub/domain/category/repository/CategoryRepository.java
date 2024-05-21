package com.artista.main.domain.category.repository;

import com.artista.main.domain.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Optional<List<CategoryEntity>> findAllByOrderByOrderNoAsc();

    Optional<CategoryEntity> findById(Long id);

}
