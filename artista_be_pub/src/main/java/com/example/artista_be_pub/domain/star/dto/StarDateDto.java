package com.artista.main.domain.star.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StarDateDto {
    /**
     * 년
     */
    private Integer year;

    /**
     * 월
     */
    List<Integer> month;
}
