package com.artista.main.domain.collection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRes {
    /**
     * 컬렉션 ID
     */
    private Long collectionId;
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
