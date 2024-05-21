package com.artista.main.domain.category.service;

import com.artista.main.domain.category.dto.response.CateListRes;
import com.artista.main.domain.category.entity.CategoryEntity;
import com.artista.main.domain.category.repository.CategoryRepository;
import com.artista.main.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.artista.main.domain.constants.ResponseCode.CATE_LIST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 리스트 조회
     * @return
     */
    @Transactional
    public List<CateListRes> getCateList(){
        List<CateListRes> cateListResList = new ArrayList<>();

        //카테고리 정보 조회
        Optional<List<CategoryEntity>> categoryEntity = categoryRepository.findAllByOrderByOrderNoAsc();
        if(categoryEntity == null || categoryEntity.isEmpty()){
            throw new CustomException(CATE_LIST_NOT_FOUND);
        }

        //응답값 설정
        for (CategoryEntity dto : categoryEntity.get()) {
            CateListRes cateListRes = new CateListRes().builder()
                    .cateId(dto.getId())
                    .cateName(dto.getCategoryName())
                    .build();

            cateListResList.add(cateListRes);
        }

        return cateListResList;
    }

}
