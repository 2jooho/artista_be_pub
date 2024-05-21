package com.artista.main.domain.star.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StarInfoDto {
    /**
     * 작품 ID
     */
    private Long galleryId;

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
}
