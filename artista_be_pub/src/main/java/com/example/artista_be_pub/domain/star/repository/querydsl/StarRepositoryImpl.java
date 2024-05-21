package com.artista.main.domain.star.repository.querydsl;

import com.artista.main.domain.star.dto.StarDatePeriodDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.artista.main.domain.star.entity.QStarEntity.starEntity;

@RequiredArgsConstructor
public class StarRepositoryImpl implements StarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<StarDatePeriodDto>> starDatePeriod(){
        List<StarDatePeriodDto> result = (List<StarDatePeriodDto>) queryFactory
                .select(Projections.constructor(StarDatePeriodDto.class,
                        starEntity.selectYear,
                        starEntity.selectMonth.min()
                        ))
                .from(starEntity)
                .groupBy(starEntity.selectYear)
                .orderBy(starEntity.selectYear.desc())
                .fetch();

        return Optional.ofNullable(result);
    }
}
