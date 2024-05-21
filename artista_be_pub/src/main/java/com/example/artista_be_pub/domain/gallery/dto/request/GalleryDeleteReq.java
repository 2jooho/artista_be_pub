package com.artista.main.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class GalleryDeleteReq {
    /**
     * 갤러리 id
     */
    @NotBlank(message = "galleryId는 필수 입니다.")
    private String galleryId;

    /**
     * 사용자 id
     */
    @NotBlank(message = "userId는 필수 입니다.")
    private String userId;
}
