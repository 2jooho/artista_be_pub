package com.artista.main.domain.main.controller;

import com.artista.main.domain.constants.BaseController;
import com.artista.main.domain.main.dto.response.MainRes;
import com.artista.main.domain.main.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.artista.main.domain.constants.ApiUrl.BASE_URL;
import static com.artista.main.domain.constants.ApiUrl.MAIN_URL;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class MainController extends BaseController {
    @Autowired
    private MainService mainService;

    @GetMapping(MAIN_URL)
    public ResponseEntity<MainRes> main(){
        MainRes mainRes = mainService.main();

        return new ResponseEntity<>(mainRes, getSuccessHeaders(), HttpStatus.OK);
    }

}
