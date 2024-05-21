package com.artista.main.domain.collection.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class CollectionUpdateReq {
    /**
     * 사용자 id
     */
    @NotBlank(message = "userId 필수 입니다.")
    private String userId;

    /**
     * 작가명
     */
    @NotBlank(message = "artistName 필수 입니다.")
    private String artistName;

    /**
     * 작품 설명
     */
    @NotBlank(message = "artistDescription 필수 입니다.")
    private String artistDescription;

}
