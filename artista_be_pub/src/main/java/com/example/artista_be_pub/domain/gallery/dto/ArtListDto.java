package com.artista.main.domain.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArtListDto {
    /**
     * 작품 ID
     */
    private Long artId;

    /**
     * 작품명
     */
    private String artName;

    /**
     * 작가명
     */
    private String artistName;

    /**
     * 작품 URL
     */
    private String artUrl;

    /**
     * 좋아요 개수
     */
    private Long likeCnt;
}
