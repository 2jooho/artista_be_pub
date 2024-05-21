package com.artista.main.domain.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPopularArtDto {
    /**
     * 작품 ID
     */
    private Long artImgId;

    /**
     * 작품 이미지 url
     */
    private String artImgUrl;

    /**
     * 작품 순위
     */
    private int artRank;
}
