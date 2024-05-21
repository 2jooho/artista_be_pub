package com.artista.main.domain.collection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollectionInfoDto {
    /**
     * 프로필 이미지 url
     */
    private String profileImg;

    /**
     * 배경 프로필 이미지 url
     */
    private String backProfileImg;

    /**
     * 작가명
     */
    private String artistName;

    /**
     * 작가 설명
     */
    private String artistDescription;

    /**
     * 나의 작품 개수
     */
    private Integer myArtCnt;

    /**
     * 좋아요한 작품 개수
     */
    private Integer likeArtCnt;
}
