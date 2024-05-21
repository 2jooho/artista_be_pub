package com.artista.main.domain.collection.controller;

import com.artista.main.domain.collection.dto.request.CollectionUpdateReq;
import com.artista.main.domain.collection.dto.request.CollectionUploadReq;
import com.artista.main.domain.collection.dto.response.CollectionRes;
import com.artista.main.domain.collection.dto.response.LikeArtRes;
import com.artista.main.domain.collection.dto.response.StarArtRes;
import com.artista.main.domain.collection.service.CollectionService;
import com.artista.main.domain.constants.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.artista.main.domain.constants.ApiUrl.*;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class CollectionController extends BaseController {
    @Autowired
    private CollectionService collectionService;

    /**
     * 컬렉션 기본정보 조회
     * @param userId
     * @return
     */
    @GetMapping(COLLECTION_INFO_URL)
    public ResponseEntity<CollectionRes> getCollectionInfo(@RequestParam String userId){
        CollectionRes collectioneRes = collectionService.getCollectionInfo(userId.toUpperCase());

        return new ResponseEntity<>(collectioneRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 좋아요 한 작품 목록 조회
     * @param userId
     * @return
     */
    @GetMapping(LIKE_ART_LIST_URL)
    public ResponseEntity<List<LikeArtRes>> getLikeArtList(@RequestParam String userId, Pageable pageable){
        List<LikeArtRes> likeArtResList = collectionService.getLikeArtList(userId.toUpperCase(), pageable);

        return new ResponseEntity<>(likeArtResList, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 콜렉션 등록
     * @param profileImageFile
     * @param backProfileImageFile
     * @param collectionUploadReq
     * @return
     * @throws IOException
     */
    @PostMapping(value = COLLECTION_UPLOAD_URL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity setCollectionUpload(@RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile
            , @RequestPart(value = "backProfileImageFile", required = false) MultipartFile backProfileImageFile
            , CollectionUploadReq collectionUploadReq) throws IOException {

        collectionService.setCollectionUpload(profileImageFile, backProfileImageFile, collectionUploadReq);

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 컬렉션 수정
     * @param profileImageFile
     * @param backProfileImageFile
     * @param collectionUpdateReq
     * @return
     * @throws IOException
     */
    @PostMapping(value = COLLECTION_UPDATE_URL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity collectionUpdate(@RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile
            , @RequestPart(value = "backProfileImageFile", required = false) MultipartFile backProfileImageFile
            , CollectionUpdateReq collectionUpdateReq) throws IOException {

        collectionService.collectionUpdate(profileImageFile, backProfileImageFile, collectionUpdateReq);

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 좋아요 한 작품 목록 조회
     * @param userId
     * @return
     */
    @GetMapping(COLLECTION_STAR_LIST_URL)
    public ResponseEntity<List<StarArtRes>> getStarArtList(@RequestParam String userId, Pageable pageable){
        List<StarArtRes> starArtResList = collectionService.getStarArtList(userId.toUpperCase(), pageable);

        return new ResponseEntity<>(starArtResList, getSuccessHeaders(), HttpStatus.OK);
    }
}
