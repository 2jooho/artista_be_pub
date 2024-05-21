package com.artista.main.domain.category.controller;

import com.artista.main.domain.category.dto.response.CateListRes;
import com.artista.main.domain.category.service.CategoryService;
import com.artista.main.domain.constants.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.artista.main.domain.constants.ApiUrl.BASE_URL;
import static com.artista.main.domain.constants.ApiUrl.CATE_LIST_URL;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 카테고리 리스트 조회
     * @return
     */
    @GetMapping(CATE_LIST_URL)
    public ResponseEntity<List<CateListRes>> categoryList(){
        List<CateListRes> cateListRes = categoryService.getCateList();
        return new ResponseEntity<>(cateListRes,getSuccessHeaders(), HttpStatus.OK);
    }

}
