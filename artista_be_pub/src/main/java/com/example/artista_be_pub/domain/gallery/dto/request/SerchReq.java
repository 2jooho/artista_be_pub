package com.artista.main.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SerchReq {
    /**
     * 검색 타입(S:전체, C:카테고리, AT:작가명, A:작품명)
     */
    @NotBlank(message = "serchType는 필수 입니다.")
    private String serchType;

    /**
     * 검색어
     */
    @NotBlank(message = "serchValue은 필수 입니다.")
    private String serchValue;
}
