package com.artista.main.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class GalleryInfoReq {
    /**
     * 사용자 id
     */
    private String userId;

    /**
     * 카테고리 id
     */
    private String categoryId;

    /**
     * 정렬타입(추천순:R, 인기순:P, 날짜순:D)
     */
    @NotBlank(message = "orderType은 필수 입니다.")
    private String orderType;

    /**
     * 조회 타입(G:갤러리(기본값), M:마이페이지)
     */
    private String selectType;
}
