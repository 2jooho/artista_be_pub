package com.artista.main.domain.main.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainRes {
    /**
     * 메인 이미지 id
     */
    private Long mainImgId;

    /** 메인 이미지 url */
    private String mainImgUrl;

//    private List<MainPopularArtDto> mainPopularArt;
}
