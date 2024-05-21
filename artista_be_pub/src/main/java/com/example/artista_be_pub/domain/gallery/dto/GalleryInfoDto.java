package com.artista.main.domain.gallery.dto;

import com.artista.main.domain.gallery.entity.ArtImgEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GalleryInfoDto {
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
    private String artist;

    /**
     * 그림 url
     */
    private String artUrl;
}
