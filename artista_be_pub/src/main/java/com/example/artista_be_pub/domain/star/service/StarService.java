package com.artista.main.domain.star.service;

import com.artista.main.domain.star.dto.StarDateDto;
import com.artista.main.domain.star.dto.StarDatePeriodDto;
import com.artista.main.domain.star.dto.StarInfoDto;
import com.artista.main.domain.star.dto.request.StarInfoReq;
import com.artista.main.domain.star.dto.response.StarDateRes;
import com.artista.main.domain.star.dto.response.StarInfoRes;
import com.artista.main.domain.star.entity.StarEntity;
import com.artista.main.domain.star.repository.StarRepository;
import com.artista.main.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.artista.main.domain.constants.ResponseCode.STAR_IMG_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Slf4j
public class StarService {
    private final StarRepository starRepository;

    @Value("${s3.img.url}")
    private String imgUrl;

    /**
     * 스타 목록 조회
     * @return
     */
    @Transactional
    public StarInfoRes getStarList(StarInfoReq starInfoReq){
        StarInfoRes starInfoRes = new StarInfoRes();
        int year, month;

        if(starInfoReq == null){
            LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            year = today.getYear();
            month = today.getMonthValue();
        } else{
            year = starInfoReq.getYear();
            month = starInfoReq.getMonth();
        }

        //년/월에 해당하는 스타작품 조회
        List<StarEntity> starList = starRepository.findBySelectYearAndSelectMonthOrderByOrderNoAsc(year, month);
        if(starList == null || starList.isEmpty()){
            throw new CustomException(STAR_IMG_NOT_FOUND);
        }
        List<StarInfoDto> starInfoDtoList = new ArrayList<>();
        for (StarEntity entity: starList) {
            StarInfoDto starInfoDto = new StarInfoDto().builder()
                    .galleryId(entity.getGalleryEntity().getId())
                    .artName(entity.getArtTitle())
                    .artistName(entity.getGalleryEntity().getUserEntity().getUserId())
                    .artUrl(imgUrl + entity.getFilePath() + entity.getFileName())
                    .build();

            starInfoDtoList.add(starInfoDto);
        }
        starInfoRes.setStarInfoDtoList(starInfoDtoList);

        return starInfoRes;
    }

    /**
     * 스타 날짜 정보 조회
     * @return
     */
    @Transactional
    public StarDateRes getStarDateList(){
        StarDateRes starDateRes = new StarDateRes();

        //스타 DB에 있는 날짜 정보 조회
        Optional<List<StarDatePeriodDto>> starDatePeriod = starRepository.starDatePeriod();
        if(starDatePeriod.get() == null || starDatePeriod.get().isEmpty()){
            throw new CustomException(STAR_IMG_NOT_FOUND);
        }

        List<StarDateDto> starDateDtoList = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        int year = today.getYear();
        int month = today.getMonthValue();
        int maxMonth = 12;
        for (StarDatePeriodDto dto: starDatePeriod.get()) {
            List<Integer> monthList = new ArrayList<>();
            if(year == dto.getSelectYear()){
                maxMonth = month;
            }
            for(int i = dto.getMinMonth(); i <= maxMonth; i++){
                monthList.add(i);
            }

            StarDateDto starDateDto = new StarDateDto().builder()
                    .year(dto.getSelectYear())
                    .month(monthList)
                    .build();

            starDateDtoList.add(starDateDto);
        }

        starDateRes.setStarDateDtoList(starDateDtoList);

        return starDateRes;
    }

}
