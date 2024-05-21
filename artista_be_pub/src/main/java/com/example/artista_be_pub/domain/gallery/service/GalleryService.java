package com.artista.main.domain.gallery.service;

import com.artista.main.domain.category.entity.CategoryEntity;
import com.artista.main.domain.category.repository.CategoryRepository;
import com.artista.main.domain.collection.entity.CollectionEntity;
import com.artista.main.domain.collection.repository.CollectionRepository;
import com.artista.main.domain.gallery.dto.ArtListDto;
import com.artista.main.domain.gallery.dto.GalleryInfoDto;
import com.artista.main.domain.gallery.dto.PopularArtDto;
import com.artista.main.domain.gallery.dto.SerchListDto;
import com.artista.main.domain.gallery.dto.request.*;
import com.artista.main.domain.gallery.dto.response.GalleryDetailRes;
import com.artista.main.domain.gallery.dto.response.GalleryInfoRes;
import com.artista.main.domain.gallery.dto.response.SerchInfoRes;
import com.artista.main.domain.gallery.entity.ArtImgEntity;
import com.artista.main.domain.gallery.entity.GalleryEntity;
import com.artista.main.domain.gallery.entity.LikeEntity;
import com.artista.main.domain.gallery.repository.ArtImgRepository;
import com.artista.main.domain.gallery.repository.GalleryRepository;
import com.artista.main.domain.gallery.repository.LikeRepository;
import com.artista.main.domain.star.entity.StarEntity;
import com.artista.main.domain.star.repository.StarRepository;
import com.artista.main.domain.user.entity.UserEntity;
import com.artista.main.domain.user.repository.UserInfoRepository;
import com.artista.main.global.error.CustomException;
import com.artista.main.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.artista.main.domain.constants.ResponseCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GalleryService {
    private final UserInfoRepository userInfoRepository;
    private final CategoryRepository categoryRepository;
    private final GalleryRepository galleryRepository;
    private final LikeRepository likeRepository;
    private final StarRepository starRepository;
    private final CollectionRepository collectionRepository;
    private final ArtImgRepository artImgRepository;

    @Value("${s3.img.url}")
    private String imgUrl;

    @Value("${s3.gallery.url}")
    private String filePath;

    private final S3Uploader s3Uploader;

    /**
     * 갤러리 업로드
     */
    @Transactional
    public void upload() {
        String userId = "artista".toUpperCase();

        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(SERVER_ERROR));
        log.info("id:" + userEntity.get().getId());

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(Long.valueOf(1));

        GalleryEntity gallery1 = new GalleryEntity();
        ArtImgEntity artImgEntity1 = new ArtImgEntity();
        artImgEntity1.of("/gallary/test", "/gallary1.png",1, gallery1);
        ArtImgEntity artImgEntity2 = new ArtImgEntity();
        artImgEntity2.of("/gallary/test", "/gallary2.png", 2,gallery1);
        List<ArtImgEntity> artImgEntityList1 = Arrays.asList(artImgEntity1, artImgEntity2);
        gallery1.galleryUpload( "gallary1", "gallary1test", userEntity.get(), null,categoryEntity.get(), artImgEntityList1);

        GalleryEntity gallery2 = new GalleryEntity();
        ArtImgEntity artImgEntity3 = new ArtImgEntity();
        artImgEntity3.of("/gallary/test", "/gallary3.png", 1,gallery2);
        ArtImgEntity artImgEntity4 = new ArtImgEntity();
        artImgEntity4.of("/gallary/test", "/gallary4.png", 2,gallery2);
        List<ArtImgEntity> artImgEntityList2 = Arrays.asList(artImgEntity3, artImgEntity4);
        gallery2.galleryUpload("gallary2", "gallary2test", userEntity.get(), null,categoryEntity.get(), artImgEntityList2);

        GalleryEntity gallery3 = new GalleryEntity();
        ArtImgEntity artImgEntity5 = new ArtImgEntity();
        artImgEntity5.of("/gallary/test", "/gallary5.png", 1,gallery3);
        ArtImgEntity artImgEntity6 = new ArtImgEntity();
        artImgEntity6.of("/gallary/test", "/gallary6.png", 2, gallery3);
        List<ArtImgEntity> artImgEntityList3 = Arrays.asList(artImgEntity5, artImgEntity6);
        gallery3.galleryUpload("gallary3", "gallary3test", userEntity.get(), null,categoryEntity.get(), artImgEntityList3);

        GalleryEntity gallery4 = new GalleryEntity();
        ArtImgEntity artImgEntity7 = new ArtImgEntity();
        artImgEntity7.of("/gallary/test", "/gallary7.png",1, gallery4);
        ArtImgEntity artImgEntity8 = new ArtImgEntity();
        artImgEntity8.of("/gallary/test", "/gallery8.jpg", 2, gallery4);
        List<ArtImgEntity> artImgEntityList4 = Arrays.asList(artImgEntity7, artImgEntity8);
        gallery4.galleryUpload("gallary4", "gallary4test", userEntity.get(), null,categoryEntity.get(), artImgEntityList4);

        GalleryEntity gallery5 = new GalleryEntity();
        ArtImgEntity artImgEntity9 = new ArtImgEntity();
        artImgEntity9.of("/gallary/test", "/LeeHeedon04.jpg", 1, gallery5);
        List<ArtImgEntity> artImgEntityList5 = Arrays.asList(artImgEntity9);
        gallery5.galleryUpload("gallary5", "gallary5test", userEntity.get(), null,categoryEntity.get(), artImgEntityList5);

        GalleryEntity gallery6 = new GalleryEntity();
        ArtImgEntity artImgEntity10 = new ArtImgEntity();
        artImgEntity10.of("/gallary/test", "/gallery10.jpg", 1, gallery6);
        List<ArtImgEntity> artImgEntityList6 = Arrays.asList(artImgEntity10);
        gallery6.galleryUpload("gallary6", "gallary6test", userEntity.get(), null,categoryEntity.get(), artImgEntityList6);

        GalleryEntity gallery7 = new GalleryEntity();
        ArtImgEntity artImgEntity11 = new ArtImgEntity();
        artImgEntity11.of("/gallary/test", "/gallery11.jpg", 1, gallery7);
        List<ArtImgEntity> artImgEntityList7 = Arrays.asList(artImgEntity11);
        gallery7.galleryUpload("gallary7", "gallary7test", userEntity.get(), null,categoryEntity.get(), artImgEntityList7);

        List<GalleryEntity> galleryEntityList = Arrays.asList(gallery1, gallery2, gallery3, gallery4, gallery5, gallery6, gallery7);
        galleryRepository.saveAll(galleryEntityList);

        Optional<UserEntity> userEntity2 = userInfoRepository.findByUserId("TEST2");
        LikeEntity likeEntity1 = new LikeEntity();
        likeEntity1.of(gallery1, userEntity.get());
        LikeEntity likeEntity2 = new LikeEntity();
        likeEntity2.of(gallery2, userEntity.get());
        LikeEntity likeEntity3 = new LikeEntity();
        likeEntity3.of(gallery3, userEntity.get());
        LikeEntity likeEntity4 = new LikeEntity();
        likeEntity4.of(gallery6, userEntity.get());
        LikeEntity likeEntity5 = new LikeEntity();
        likeEntity5.of(gallery4, userEntity.get());
        LikeEntity likeEntity6 = new LikeEntity();
        likeEntity6.of(gallery5, userEntity.get());
        LikeEntity likeEntity7 = new LikeEntity();
        likeEntity7.of(gallery7, userEntity.get());
        LikeEntity likeEntity8 = new LikeEntity();
        likeEntity8.of(gallery7, userEntity2.get());

        List<LikeEntity> likeEntityList = Arrays.asList(likeEntity1, likeEntity2, likeEntity3, likeEntity4, likeEntity5, likeEntity6, likeEntity7, likeEntity8);
        likeRepository.saveAll(likeEntityList);

        StarEntity starEntity1 = new StarEntity();
        starEntity1.of("best1", "best1test", "/star/2023-09", "/best1.png", 2023, 12, 1, gallery1);
        StarEntity starEntity2 = new StarEntity();
        starEntity2.of("best2", "best2test", "/star/2023-09", "/test.jpeg", 2023, 12, 2, gallery2);
        StarEntity starEntity3 = new StarEntity();
        starEntity3.of("best3", "best3test", "/star/2023-09", "/testProfile.png", 2023, 12, 3, gallery3);
        List<StarEntity> starEntityList = Arrays.asList(starEntity1, starEntity2, starEntity3);
        starRepository.saveAll(starEntityList);

        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.of("/star/2023-09", "best1.png", "/star/2023-09", "best1.png", "best1111", userEntity.get());
        collectionRepository.save(collectionEntity);
    }

    /**
     * 갤러리 최초 정보 조회
     * @return
     */
    @Transactional
    public GalleryInfoRes getFirstInfo() {
        GalleryInfoRes galleryInfoRes = new GalleryInfoRes();
        long size = 3;
        //인기 작품 3개 조회
        Optional<List<GalleryInfoDto>> galleryInfoDtoList = galleryRepository.popularGalleryInfo(size);

        //인기 작품 응답값 설정
        if(galleryInfoDtoList.isPresent()){
            List<PopularArtDto> popularArtList = new ArrayList<>();
            for (GalleryInfoDto dto : galleryInfoDtoList.get()) {
                PopularArtDto popularArtDto = new PopularArtDto().builder()
                        .artId(dto.getArtId())
                        .artName(dto.getArtName())
                        .artistName(dto.getArtist())
                        .artUrl(imgUrl + dto.getArtUrl())
                        .build();

                popularArtList.add(popularArtDto);
            }

            galleryInfoRes.setPopularArtList(popularArtList);
        }

        //작품 목록 조회
        int pageSize = 15;
        int page = 0;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<GalleryEntity> galleryEntityPage = galleryRepository.findAll(pageable);
        if(galleryEntityPage == null || galleryEntityPage.isEmpty()){
            throw new CustomException(NO_DATA);
        }

        List<ArtListDto> artListDtoList = new ArrayList<>();
        for (GalleryEntity entity : galleryEntityPage) {
            ArtListDto artListDto = new ArtListDto().builder()
                    .artId(entity.getId())
                    .artName(entity.getArtTitle())
                    .artistName(entity.getUserEntity().getUserId())
                    .artUrl(imgUrl + entity.getArtImgEntityList().get(0).getFilePath() + entity.getArtImgEntityList().get(0).getFileName())
                    .build();

            artListDtoList.add(artListDto);
        }
        galleryInfoRes.setArtListDtoList(artListDtoList);

        return galleryInfoRes;
    }

    /**
     * 갤러리 목록 조회
     * @param galleryInfoReq
     * @param pageable
     * @return
     */
    @Transactional
    public GalleryInfoRes getGalleryList(GalleryInfoReq galleryInfoReq, Pageable pageable){
        GalleryInfoRes galleryInfoRes = new GalleryInfoRes();
        String userId = galleryInfoReq.getUserId();
        String selectType = StringUtils.defaultString(galleryInfoReq.getSelectType(), "G");
        String orderType = galleryInfoReq.getOrderType().toUpperCase();

        String categoryIdStr = galleryInfoReq.getCategoryId();
        Long categoryId = categoryIdStr != null && !categoryIdStr.isEmpty() ? Long.valueOf(categoryIdStr) : 0L;

        //컬렉션 목록 조회
        if(selectType.equals("M") && (!userId.isBlank() || !userId.equals(""))){
            userId = userId.toUpperCase();
            List<ArtListDto> artListDtoList = new ArrayList<>();
            Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
            userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            Page<GalleryEntity> galleryEntityPage = galleryRepository.findAllByUserEntityOrderByIdDesc(userEntity.get(), pageable);

            if(galleryEntityPage == null || galleryEntityPage.isEmpty()){
                throw new CustomException(NO_DATA);
            }

            for (GalleryEntity entity : galleryEntityPage) {
                ArtListDto artListDto = new ArtListDto().builder()
                        .artId(entity.getId())
                        .artName(entity.getArtTitle())
                        .artistName(entity.getUserEntity().getUserId())
                        .artUrl(imgUrl + entity.getArtImgEntityList().get(0).getFilePath() + entity.getArtImgEntityList().get(0).getFileName())
                        .build();

                artListDtoList.add(artListDto);
            }
            galleryInfoRes.setArtListDtoList(artListDtoList);
        //갤러리 목록 조회
        }else {
            Optional<List<ArtListDto>> artListDtoList = galleryRepository.getGalleryList(categoryId, orderType, pageable);
            galleryInfoRes.setArtListDtoList(artListDtoList.get());
        }


//        Page<GalleryEntity> galleryEntityPage;
//        if(!userId.isBlank() || !userId.equals("")){
//            Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
//            userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
//
//            galleryEntityPage = galleryRepository.findAllByUserEntityOrderByIdDesc(userEntity.get(), pageable);
//        }else {
//            galleryEntityPage = galleryRepository.findAllByOrderByIdDesc(pageable);
//        }
//
//        if(galleryEntityPage == null || galleryEntityPage.isEmpty()){
//            throw new CustomException(NO_DATA);
//        }
//
//        for (GalleryEntity entity : galleryEntityPage) {
//            ArtListDto artListDto = new ArtListDto().builder()
//                    .artId(entity.getId())
//                    .artName(entity.getArtTitle())
//                    .artistName(entity.getUserEntity().getUserId())
//                    .artUrl(imgUrl + entity.getArtImgEntityList().get(0).getFilePath() + entity.getArtImgEntityList().get(0).getFileName())
//                    .build();
//
//            artListDtoList.add(artListDto);
//        }




        return galleryInfoRes;
    }

    /**
     * 작품 상세 정보 조회
     * @param galleryDetailReq
     * @return
     */
    @Transactional
    public GalleryDetailRes getGalleryDetail(GalleryDetailReq galleryDetailReq) {
        log.info("galleryDetailReq:{}", galleryDetailReq.getArtId());
        Long artId = Long.valueOf(galleryDetailReq.getArtId());
        log.info("galleryDetailReq:{}", galleryDetailReq.getArtId());

        //갤러리 정보 조회
        Optional<GalleryEntity> galleryEntity = galleryRepository.findById(artId);
        galleryEntity.orElseThrow(() -> new CustomException(NO_DATA));

        //작품 좋아요 개수 조회
        Optional<Integer> likeCnt = likeRepository.countByGalleryEntity(galleryEntity.get());

        CollectionEntity collectionEntity = galleryEntity.get().getUserEntity().getCollectionEntity();
        CategoryEntity categoryEntity = galleryEntity.get().getCategoryEntity();

        //작품 정보 조회
        List<ArtImgEntity> artImgEntityList = artImgRepository.findByGalleryEntityOrderByOrderNo(galleryEntity.get());
        List<GalleryInfoDto> galleryInfoDtoList = new ArrayList<>();
        for (ArtImgEntity entity : artImgEntityList) {
            GalleryInfoDto galleryInfoDto = new GalleryInfoDto().builder()
                    .artId(entity.getId())
                    .artUrl(imgUrl + entity.getFilePath() + entity.getFileName())
                    .build();

            galleryInfoDtoList.add(galleryInfoDto);
        }

        //사용자 정보 조회
        String userId = galleryEntity.get().getUserEntity().getUserId();
        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //사용자 좋아요 여부
        Optional<LikeEntity> likeEntity = likeRepository.findByUserEntityAndGalleryEntity(userEntity.get(), galleryEntity.get());
        Boolean isLike = likeEntity.isPresent() ? true : false;

        GalleryDetailRes galleryDetailRes = new GalleryDetailRes().builder()
                .artName(galleryEntity.get().getArtTitle())
                .artDescription(galleryEntity.get().getArtDescription())
                .likeCnt(likeCnt.get())
                .artistName(galleryEntity.get().getUserEntity().getNickName())
                .artistInstaAddr(collectionEntity.getArtistInstaAddr() != null ? collectionEntity.getArtistInstaAddr() : null)
                .profileUrl(imgUrl + collectionEntity.getProfilePath() + collectionEntity.getProfileName())
                .categoryId(categoryEntity.getId())
                .categoryName(categoryEntity.getCategoryName())
                .galleryInfoDtoList(galleryInfoDtoList)
                .recommandArtDtoList(null)
                .isLike(isLike)
                .build();

        return galleryDetailRes;
    }

    /**
     * 작품 등록(이미지 최대 3개)
     * @param images
     * @param galleryUploadReq
     */
    @Transactional
    public void setGalleryUpload(List<MultipartFile> images, GalleryUploadReq galleryUploadReq) throws IOException {
        String userId = galleryUploadReq.getUserId().toUpperCase();
        //사용자 정보 조회
        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //카테고리 정보 조회
        Long categoryId = Long.valueOf(galleryUploadReq.getCategoryId());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        categoryEntity.orElseThrow(() -> new CustomException(CATE_LIST_NOT_FOUND));

        //이미지 업로드 및 저장
        GalleryEntity galleryEntity = new GalleryEntity();
        List<ArtImgEntity> artImgEntityList = new ArrayList<>();
        int cnt = 1;
        for (MultipartFile image : images) {
            //s3업로드 및 이미지 url리턴
            String s3ImgUrl = s3Uploader.upload(filePath, s3Uploader.getUuidFileName(image), image);
            //s3에 올라간 이미지는 uuid가 포함되어 있어서 새롭게 추출 필요
            String[] temp = s3ImgUrl.split("/");
            String fileName = "/" + temp[temp.length-1];

            ArtImgEntity artImgEntity = new ArtImgEntity();
            artImgEntity.of(filePath, fileName, cnt, galleryEntity);
            artImgEntityList.add(artImgEntity);
            cnt++;
        }
        //갤러리 저장
        String artName = galleryUploadReq.getArtName();
        String artDescription = galleryUploadReq.getArtDescription();
        galleryEntity.galleryUpload(artName, artDescription, userEntity.get(), null, categoryEntity.get(), artImgEntityList);
        galleryRepository.save(galleryEntity);
    }

    /**
     * 작품 수정(이미지 최대 3개)
     * @param newImageFile
     * @param galleryUpdateReq
     * @throws IOException
     */
    @Transactional
    public void setGalleryUpdate(List<MultipartFile> newImageFile, GalleryUpdateReq galleryUpdateReq) throws IOException {
//        List<String> artIdList = Arrays.asList(galleryUpdateReq.getChangeImageArray().split(","));
//        List<Long> artIdLongList = artIdList.stream()
//                .map(Long::valueOf)
//                .collect(Collectors.toList());

        //갤러리 조회
        Long galleryId = Long.valueOf(galleryUpdateReq.getGalleryId());
        Optional<GalleryEntity> galleryEntity = galleryRepository.findById(galleryId);
        galleryEntity.orElseThrow(() -> new CustomException(NO_DATA));

        //카테고리 정보 조회
        Long categoryId = Long.valueOf(galleryUpdateReq.getCategoryId());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        categoryEntity.orElseThrow(() -> new CustomException(CATE_LIST_NOT_FOUND));

        //갤러리에 포함된 작품이미지 조회
        List<ArtImgEntity> artImgEntityList = artImgRepository.findByGalleryEntity(galleryEntity.get());
        if(artImgEntityList == null || artImgEntityList.isEmpty()){
            throw new CustomException(NO_DATA);
        }

        //기존 이미지 S3 삭제, 이미지 데이터 삭제
        for (ArtImgEntity entity : artImgEntityList) {
            s3Uploader.delete(entity.getFilePath(), entity.getFileName());
            artImgRepository.delete(entity);
        }

        //이미지 업로드 및 저장
        int i = 1;
        for (MultipartFile image : newImageFile) {
            if(!image.isEmpty()){
                //s3업로드 및 이미지 url리턴
                String s3ImgUrl = s3Uploader.upload(filePath, s3Uploader.getUuidFileName(image), image);
                //s3에 올라간 이미지는 uuid가 포함되어 있어서 새롭게 추출 필요
                String[] temp = s3ImgUrl.split("/");
                String fileName = "/" + temp[temp.length-1];
                ArtImgEntity entity = new ArtImgEntity();
                entity.of(filePath, fileName, i, galleryEntity.get());
                artImgRepository.save(entity);
                i++;
//                for (ArtImgEntity artImgEntity : artImgEntityList) {
//                    if(artImgEntity.getId() == artIdLongList.get(i)){
//                        int orderNo = artImgEntity.getOrderNo();
//                        //기존 엔티티 삭제
//                        artImgRepository.delete(artImgEntity);
//                        //신규 엔티티 등록
//                        ArtImgEntity entity = new ArtImgEntity();
//                        entity.of(filePath, fileName, orderNo, galleryEntity.get());
//                        artImgRepository.save(entity);
//                    }
//                }
            }
        }

        //갤러리 업데이트
        String artName = galleryUpdateReq.getArtName();
        String artDescription = galleryUpdateReq.getArtDescription();
        galleryEntity.get().update(artName, artDescription, categoryEntity.get());
    }

    /**
     * 작품 삭제
     * @param galleryId
     */
    @Transactional
    public void deleteGallery(String galleryId) {
        //갤러리 조회
        Optional<GalleryEntity> galleryEntity = galleryRepository.findById(Long.valueOf(galleryId));
        galleryEntity.orElseThrow(() -> new CustomException(NO_DATA));

        List<ArtImgEntity> artImgEntityList = galleryEntity.get().getArtImgEntityList();
        //데이터 삭제
        galleryRepository.delete(galleryEntity.get());

        //s3 이미지 삭제
        for (ArtImgEntity entity : artImgEntityList) {
            s3Uploader.delete(entity.getFilePath(), entity.getFileName());
        }
    }

    /**
     * 검색(S:전체, C:카테고리, AT:작가명, A:작품명)
     * @param serchReq
     * @return
     */
    @Transactional
    public SerchInfoRes getSerch(SerchReq serchReq, Pageable pageable) {
        SerchInfoRes serchInfoRes = new SerchInfoRes();
        String serchType = serchReq.getSerchType();
        String serchValue = serchReq.getSerchValue();

        Optional<List<SerchListDto>> artListDtoList = galleryRepository.serch(serchType, serchValue, pageable);
        //전체 검색
//        if(serchType.equals("S")){
//            List<GalleryEntity> galleryEntityList = galleryRepository.findByArtTitleContaining(serchValue, pageable);
//            for (GalleryEntity gallery : galleryEntityList) {
//                List<ArtImgEntity> artImgEntityList = gallery.getArtImgEntityList();
//                ArtListDto artListDto = new ArtListDto().builder()
//                        .artId(gallery.getId())
//                        .artName(gallery.getArtTitle())
//                        .artUrl(imgUrl + artImgEntityList.get(0).getFilePath() + artImgEntityList.get(0).getFileName())
//                        .build();
//                artListDtoList.add(artListDto);
//            }
//
//            //카테고리 검색
//        }else if(serchType.equals("C")){
//
//            //작가명
//        }else if(serchType.equals("AT")){
//
//            //그림명
//        }else if(serchType.equals("A")) {
//
//        }

        serchInfoRes.setArtListDtoList(artListDtoList.get());

        return serchInfoRes;
    }

    /**
     * 좋아요 업데이트
     * @param likeReq
     */
    @Transactional
    public void updateLike(LikeReq likeReq){
        String userId = likeReq.getUserId().toUpperCase();
        Long artId = Long.valueOf(likeReq.getArtId());
        Boolean isLike = Boolean.valueOf(likeReq.getIsLike());

        //사용자 조회
        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //갤러리 정보 조회
        Optional<GalleryEntity> galleryEntity = galleryRepository.findById(artId);
        galleryEntity.orElseThrow(() -> new CustomException(NO_DATA));
        //좋아요 여부 조회
        Optional<LikeEntity> likeEntity = likeRepository.findByUserEntityAndGalleryEntity(userEntity.get(), galleryEntity.get());
        if(!isLike && likeEntity.isPresent()){
            likeRepository.delete(likeEntity.get());
        }else if (isLike && !likeEntity.isPresent()){
            LikeEntity setLikeEntity = new LikeEntity();
            setLikeEntity.of(galleryEntity.get(), userEntity.get());
            likeRepository.save(setLikeEntity);
        }else{
            throw new CustomException(INVALID_REQUEST);
        }
    }

}
