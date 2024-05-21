package com.artista.main.domain.star.repository.querydsl;

import com.artista.main.domain.star.dto.StarDatePeriodDto;

import java.util.List;
import java.util.Optional;

public interface StarRepositoryCustom {
    /**
     * 스타작품 날짜 기간
     * @return
     */
    Optional<List<StarDatePeriodDto>> starDatePeriod();

}
