package com.artista.main.domain.category.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateListRes {
    /** 카테고리 id */
    private Long cateId;

    /** 카테고리 명 */
    private String cateName;
}
