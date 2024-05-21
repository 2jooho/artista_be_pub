package com.artista.main.domain.main.service;

import com.artista.main.domain.gallery.repository.GalleryRepository;
import com.artista.main.domain.main.dto.response.MainRes;
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
import java.util.List;

import static com.artista.main.domain.constants.ResponseCode.STAR_IMG_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {
    private final StarRepository starRepository;

    private final GalleryRepository galleryRepository;

    @Value("${s3.img.url}")
    private String imgUrl;

    /**
     * 메인 조회
     * @return
     */
    @Transactional
    public MainRes main(){
        MainRes mainRes = new MainRes();
//        long size = 9; //개수 조회

        log.info("메인 조회");
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        int year = today.getYear();
        int monthValue = today.getMonthValue();

        //메인 star 조회
        List<StarEntity> starList = starRepository.findBySelectYearAndSelectMonth(year, monthValue);
        if(starList == null || starList.isEmpty()){
            throw new CustomException(STAR_IMG_NOT_FOUND);
        }

        int randomInt = (int) (Math.random() * starList.size());

        mainRes.setMainImgId(starList.get(randomInt).getId());
        String starUrl = starList.get(randomInt).getFilePath() + starList.get(randomInt).getFileName();
        mainRes.setMainImgUrl(imgUrl + starUrl);


//        log.info("인기 작품 조회");
//        //인기 작품 최대 9개 조회
//        Optional<List<GalleryInfoDto>> galleryInfoDtoList = galleryRepository.popularGalleryInfo(size);
//
//        if(galleryInfoDtoList.isPresent()){
//            List<MainPopularArtDto> mainPopularArtDtoList = new ArrayList<>();
//            int i = 1;
//            for (GalleryInfoDto dto : galleryInfoDtoList.get()) {
//                MainPopularArtDto mainPopularArtDto = new MainPopularArtDto().builder()
//                        .artImgId(dto.getArtId())
//                        .artImgUrl(imgUrl + dto.getArtUrl())
//                        .artRank(i)
//                        .build();
//                i++;
//
//                mainPopularArtDtoList.add(mainPopularArtDto);
//            }
//
//            mainRes.setMainPopularArt(mainPopularArtDtoList);
//        }

        return mainRes;
    }

}
