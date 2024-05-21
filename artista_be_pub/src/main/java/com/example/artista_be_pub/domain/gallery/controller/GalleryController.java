package com.artista.main.domain.gallery.controller;

import com.artista.main.domain.constants.BaseController;
import com.artista.main.domain.gallery.dto.request.*;
import com.artista.main.domain.gallery.dto.response.GalleryDetailRes;
import com.artista.main.domain.gallery.dto.response.GalleryInfoRes;
import com.artista.main.domain.gallery.dto.response.SerchInfoRes;
import com.artista.main.domain.gallery.service.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.artista.main.domain.constants.ApiUrl.*;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class GalleryController extends BaseController {
    @Autowired
    private GalleryService galleryService;

    /**
     * 테스트용 갤러리 업로드
     * @return
     */
    @GetMapping(GALLERY_UPLOAD_URL_TEST)
    public ResponseEntity upload(){
        galleryService.upload();

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 갤러리 최초 정보 조회
     * @return
     */
    @GetMapping(FIRST_INFO_URL)
    public ResponseEntity<GalleryInfoRes> getFirstInfo(){
        GalleryInfoRes galleryInfoRes= galleryService.getFirstInfo();

        return new ResponseEntity<>(galleryInfoRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 갤러리 목록 조회
     * @param pageable
     * @return
     */
    @GetMapping(GALLERY_LIST_URL)
    public ResponseEntity<GalleryInfoRes> getGalleryList(@Valid GalleryInfoReq galleryInfoReq, Pageable pageable){
        log.info("getOrderType:{}",galleryInfoReq.getOrderType());
        GalleryInfoRes galleryInfoRes = galleryService.getGalleryList(galleryInfoReq, pageable);

        return new ResponseEntity<>(galleryInfoRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 작품 상세정보 조회
     * @param galleryDetailReq
     * @return
     */
    @GetMapping(GALLERY_DETAIL_URL)
    public ResponseEntity<GalleryDetailRes> getGalleryDetail(GalleryDetailReq galleryDetailReq){
        GalleryDetailRes galleryDetailRes = galleryService.getGalleryDetail(galleryDetailReq);

        return new ResponseEntity<>(galleryDetailRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 작품 등록(이미지 최대 3개)
     * @param imageFile
     * @return
     */
    @PostMapping(value = GALLERY_UPLOAD_URL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity setGalleryUpload(@RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFile
                                                                , GalleryUploadReq galleryUploadReq) throws IOException {
        galleryService.setGalleryUpload(imageFile, galleryUploadReq);

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 작품 수정(이미지 최대 3개)
     * @param newImageFile
     * @param galleryUpdateReq
     * @return
     * @throws IOException
     */
    @PostMapping(value = GALLERY_UPDATE_URL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity setGalleryUpdate(@RequestPart(value = "imageFile", required = false) List<MultipartFile> newImageFile
            , GalleryUpdateReq galleryUpdateReq) throws IOException {
        galleryService.setGalleryUpdate(newImageFile, galleryUpdateReq);

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 작품 삭제
     * @param galleryId
     * @return
     * @throws IOException
     */
    @DeleteMapping(GALLERY_DELETE_URL)
    public ResponseEntity deleteGallery(@PathVariable String galleryId) throws IOException {
        galleryService.deleteGallery(galleryId);

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 검색
     * @param serchReq
     * @param pageable
     * @return
     */
    @GetMapping(SERCH_URL)
    public ResponseEntity<SerchInfoRes> getSerch(@Valid SerchReq serchReq, Pageable pageable){
        SerchInfoRes serchInfoRes = galleryService.getSerch(serchReq, pageable);

        return new ResponseEntity<>(serchInfoRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 작품 좋아요 업데이트
     * @param likeReq
     * @return
     */
    @GetMapping(ART_LIKE_URL)
    public ResponseEntity updateLike(@Valid LikeReq likeReq){
        galleryService.updateLike(likeReq);

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

}
