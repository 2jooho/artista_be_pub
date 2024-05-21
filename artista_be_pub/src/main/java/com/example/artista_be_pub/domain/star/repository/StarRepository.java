package com.artista.main.domain.star.repository;

import com.artista.main.domain.star.entity.StarEntity;
import com.artista.main.domain.star.repository.querydsl.StarRepositoryCustom;
import com.artista.main.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<StarEntity, String>, StarRepositoryCustom {
    /**
     * 해당 년/월 작품 조회
     * @param year
     * @param month
     * @return
     */
    List<StarEntity> findBySelectYearAndSelectMonth(int year, int month);

    /**
     * 해당 년/월 작품 조회 (순번 정렬)
     * @return
     */
    List<StarEntity> findBySelectYearAndSelectMonthOrderByOrderNoAsc(int year, int month);

    /**
     * 사용자의 스타 목록 조회
     * @return
     */
    List<StarEntity> findByUserEntityOrderByRgsttDtmDesc(UserEntity userEntity);
}
