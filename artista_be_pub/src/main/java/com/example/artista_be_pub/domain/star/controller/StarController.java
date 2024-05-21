package com.artista.main.domain.star.controller;

import com.artista.main.domain.constants.BaseController;
import com.artista.main.domain.star.dto.request.StarInfoReq;
import com.artista.main.domain.star.dto.response.StarDateRes;
import com.artista.main.domain.star.dto.response.StarInfoRes;
import com.artista.main.domain.star.service.StarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.artista.main.domain.constants.ApiUrl.*;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class StarController extends BaseController {
    @Autowired
    private StarService starService;

    /**
     * 스타 정보 조회
     * @return
     */
    @GetMapping(STAR_LIST_URL)
    public ResponseEntity<StarInfoRes> starList(StarInfoReq starInfoReq){
        StarInfoRes starInfoRes = starService.getStarList(starInfoReq);

        return new ResponseEntity<>(starInfoRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 스타 날짜 정보 조회
     * @return
     */
    @GetMapping(STAR_DATE_LIST_URL)
    public ResponseEntity<StarDateRes> starDateList(){
        StarDateRes starDateRes = starService.getStarDateList();

        return new ResponseEntity<>(starDateRes, getSuccessHeaders(), HttpStatus.OK);
    }

}
