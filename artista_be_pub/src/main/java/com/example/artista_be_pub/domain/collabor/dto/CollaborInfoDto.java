package com.artista.main.domain.collabor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaborInfoDto {
    /**
     * 작품 ID
     */
    private Long artId;

    /**
     * 작품명
     */
    private String artName;

    /**
     * 작품url
     */
    private String artUrl;
}
