package com.artista.main.domain.gallery.repository;

import com.artista.main.domain.gallery.entity.ArtImgEntity;
import com.artista.main.domain.gallery.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtImgRepository extends JpaRepository<ArtImgEntity, String> {
    /**
     * 갤러리에 포함된 작품이미지 목록 조회
     * @param galleryEntity
     * @return
     */
    List<ArtImgEntity> findByGalleryEntity(GalleryEntity galleryEntity);
    List<ArtImgEntity> findByGalleryEntityAndIdIn(GalleryEntity galleryEntity, List<Long> artIdList);

    List<ArtImgEntity> findByGalleryEntityOrderByOrderNo(GalleryEntity galleryEntity);

}
