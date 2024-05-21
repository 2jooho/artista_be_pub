package com.artista.main.domain.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PopularArtDto {
    /**
     * 그림 ID
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
     * 그림 URL 목록
     */
    private String artUrl;
}
