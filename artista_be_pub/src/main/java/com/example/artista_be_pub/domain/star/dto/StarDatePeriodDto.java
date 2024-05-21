package com.artista.main.domain.star.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StarDatePeriodDto {
    /**
     * 선정 년도
     */
    private Integer selectYear;

//    /**
//     * 선정 년도 월 개수
//     */
//    private Integer countMonth;

    /**
     * 최소 월
     */
    private Integer minMonth;

}
