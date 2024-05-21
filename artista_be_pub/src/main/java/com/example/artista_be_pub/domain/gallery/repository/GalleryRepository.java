package com.artista.main.domain.gallery.repository;

import com.artista.main.domain.gallery.entity.GalleryEntity;
import com.artista.main.domain.gallery.repository.querydsl.GalleryRepositoryCustom;
import com.artista.main.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<GalleryEntity, String>, GalleryRepositoryCustom {

    /**
     * 페이지 형태의 갤러리 목록 조회
     * @param pageable the pageable to request a paged result, can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @return
     */
    Page<GalleryEntity> findAllByOrderByIdDesc(Pageable pageable);

    Page<GalleryEntity> findAllByUserEntityOrderByIdDesc(UserEntity userEntity, Pageable pageable);

    Optional<Integer> countByUserEntity(UserEntity userEntity);

    Optional<GalleryEntity> findById(Long id);

    List<GalleryEntity> findByArtTitleContaining(String value, Pageable pageable);
}
