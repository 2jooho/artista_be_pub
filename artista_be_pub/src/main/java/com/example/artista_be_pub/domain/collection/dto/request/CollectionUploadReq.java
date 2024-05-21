package com.artista.main.domain.collection.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class CollectionUploadReq {
    /**
     * 사용자 id
     */
    @NotBlank(message = "userId는 필수 입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "아이디는 2~20자리 영문, 한굴, 숫자 조합이여야 합니다.")
    private String userId;

//    /**
//     * 작가명
//     */
//    @NotBlank(message = "artistName 필수 입니다.")
//    private String artistName;

    /**
     * 작품 설명
     */
    @NotBlank(message = "artistDescription 필수 입니다.")
    private String artistDescription;

}
