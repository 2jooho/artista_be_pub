package com.artista.main.domain.gallery.repository;

import com.artista.main.domain.gallery.entity.GalleryEntity;
import com.artista.main.domain.gallery.entity.LikeEntity;
import com.artista.main.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, String> {
    /**
     * 사용자가 좋아요 한 개수 조회
     * @param userEntity
     * @return
     */
    Optional<Integer> countByUserEntity(UserEntity userEntity);

    /**
     * 갤러리에 해당하는 좋아요 개수 조회
     * @param galleryEntity
     * @return
     */
    Optional<Integer> countByGalleryEntity(GalleryEntity galleryEntity);

    /**
     * 사용자의 갤러리 좋아요 정보 조회
     * @param userEntity
     * @param galleryEntity
     * @return
     */
    Optional<LikeEntity> findByUserEntityAndGalleryEntity(UserEntity userEntity, GalleryEntity galleryEntity);
}
