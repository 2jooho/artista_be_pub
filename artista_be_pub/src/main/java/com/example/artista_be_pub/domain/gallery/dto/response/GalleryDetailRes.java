package com.artista.main.domain.gallery.dto.response;

import com.artista.main.domain.gallery.dto.GalleryInfoDto;
import com.artista.main.domain.gallery.dto.RecommandArtDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryDetailRes {
    /**
     * 작품명
     */
    private String artName;

    /**
     * 작품설명
     */
    private String artDescription;

    /**
     * 좋아요 개수
     */
    private Integer likeCnt;

    /**
     * 작가명
     */
    private String artistName;

    /**
     * 작가 인스타그램 주소
     */
    private String artistInstaAddr;

    /**
     * 작가 프로필
     */
    private String profileUrl;

    /**
     * 카테고리 ID
     */
    private Long categoryId;

    /**
     * 카테고리명
     */
    private String categoryName;

    /**
     * 작품 이미지 목록
     */
    public List<GalleryInfoDto> galleryInfoDtoList;

    /**
     * 비슷한 추천 작품 목록
     */
    public List<RecommandArtDto> recommandArtDtoList;

    /**
     * 좋아요 여부
     */
    public Boolean isLike;
}
