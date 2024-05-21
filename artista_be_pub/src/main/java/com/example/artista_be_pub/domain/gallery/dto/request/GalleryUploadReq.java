package com.artista.main.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GalleryUploadReq {
    /**
     * 사용자 id
     */
    @NotBlank(message = "userId는 필수 입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "아이디는 2~20자리 영문, 한굴, 숫자 조합이여야 합니다.")
    private String userId;

    /**
     * 작품명
     */
    @NotBlank(message = "artName은 필수 입니다.")
    private String artName;

    /**
     * 작품 설명
     */
    @NotBlank(message = "artDescription은 필수 입니다.")
    private String artDescription;

    /**
     * 카테고리 ID
     */
    @NotBlank(message = "artDescription은 필수 입니다.")
    private String categoryId;

}
