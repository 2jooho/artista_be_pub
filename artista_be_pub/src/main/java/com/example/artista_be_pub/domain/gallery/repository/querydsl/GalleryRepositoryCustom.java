package com.artista.main.domain.gallery.repository.querydsl;

import com.artista.main.domain.gallery.dto.ArtListDto;
import com.artista.main.domain.gallery.dto.GalleryInfoDto;
import com.artista.main.domain.gallery.dto.SerchListDto;
import com.artista.main.domain.user.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GalleryRepositoryCustom {
    /**
     * 인기 갤러리 조회
     * @return
     */
    Optional<List<GalleryInfoDto>> popularGalleryInfo(long size);

    /**
     * 좋아요 한 작품 목록 조회
     * @param pageable
     * @return
     */
    Optional<List<GalleryInfoDto>> getLikeArtList(UserEntity userEntity, Pageable pageable);

    /**
     * 검색
     * @param pageable
     * @return
     */
    Optional<List<SerchListDto>> serch(String serchType, String serchValue, Pageable pageable);

    /**
     * 갤러리 목록 조회
     * @param categoryId
     * @param orderType
     * @param pageable
     * @return
     */
    Optional<List<ArtListDto>> getGalleryList(Long categoryId, String orderType, Pageable pageable);
}
