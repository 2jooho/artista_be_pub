package com.artista.main.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GalleryDetailReq {
    /**
     * 사용자 id
     */
//    private String userId;

    /**
     * 작품 id
     */
    private String artId;
}
