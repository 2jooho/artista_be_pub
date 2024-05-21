package com.artista.main.domain.collabor.controller;

import com.artista.main.domain.collabor.dto.response.CollaborInfoRes;
import com.artista.main.domain.collabor.service.CollaborService;
import com.artista.main.domain.constants.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.artista.main.domain.constants.ApiUrl.*;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class CollaborController extends BaseController {
    @Autowired
    private CollaborService collaborService;

    /**
     * 콜라보 목록 조회
     * @return
     */
    @GetMapping(COLLABOR_LIST_URL)
    public ResponseEntity<CollaborInfoRes> getCollaborList(@PageableDefault(size = 15) Pageable pageable){
        CollaborInfoRes collaborInfoRes = collaborService.getCollaborList(pageable);

        return new ResponseEntity<>(collaborInfoRes, getSuccessHeaders(), HttpStatus.OK);
    }
}
