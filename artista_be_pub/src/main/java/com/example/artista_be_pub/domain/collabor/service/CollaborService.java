package com.artista.main.domain.collabor.service;

import com.artista.main.domain.collabor.dto.CollaborInfoDto;
import com.artista.main.domain.collabor.dto.response.CollaborInfoRes;
import com.artista.main.domain.collabor.entity.CollaborEntity;
import com.artista.main.domain.collabor.repository.CollaborRepository;
import com.artista.main.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.artista.main.domain.constants.ResponseCode.NO_DATA;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollaborService {
    private final CollaborRepository collaborRepository;

    @Value("${s3.img.url}")
    private String imgUrl;

    /**
     * 콜라보 목록 조회
     * @param pageable
     * @return
     */
    @Transactional
    public CollaborInfoRes getCollaborList(Pageable pageable) {
        CollaborInfoRes collaborInfoRes = new CollaborInfoRes();

        //콜라보 페이징 조회
        Page<CollaborEntity> collaborEntityPage = collaborRepository.findAll(pageable);
        if(collaborEntityPage == null || collaborEntityPage.isEmpty()){
            throw new CustomException(NO_DATA);
        }

        //응답값 설정
        List<CollaborInfoDto> collaborInfoDtoList = new ArrayList<>();
        for (CollaborEntity entity: collaborEntityPage) {
            CollaborInfoDto collaborInfoDto = new CollaborInfoDto().builder()
                    .artId(entity.getId())
                    .artName(entity.getArtTitle())
                    .artUrl(imgUrl + entity.getFilePath() + entity.getFileName())
                    .build();

            collaborInfoDtoList.add(collaborInfoDto);
        }

        collaborInfoRes.setCollaborInfoDtoList(collaborInfoDtoList);

        return collaborInfoRes;
    }
}
