package com.artista.main.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeReq {
    /**
     * 사용자 id
     */
    @NotBlank(message = "userId는 필수 입니다.")
    private String userId;

    /**
     * 작품 id
     */
    @NotBlank(message = "artId는 필수 입니다.")
    private String artId;

    /**
     * 좋아요 여부
     */
    @NotBlank(message = "isLike는 필수 입니다.")
    private String isLike;
}
