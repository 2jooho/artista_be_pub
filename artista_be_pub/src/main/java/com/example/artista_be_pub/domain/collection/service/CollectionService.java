package com.artista.main.domain.collection.service;

import com.artista.main.domain.collection.dto.request.CollectionUpdateReq;
import com.artista.main.domain.collection.dto.request.CollectionUploadReq;
import com.artista.main.domain.collection.dto.response.CollectionRes;
import com.artista.main.domain.collection.dto.response.LikeArtRes;
import com.artista.main.domain.collection.dto.response.StarArtRes;
import com.artista.main.domain.collection.entity.CollectionEntity;
import com.artista.main.domain.collection.repository.CollectionRepository;
import com.artista.main.domain.gallery.dto.GalleryInfoDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static com.artista.main.domain.constants.ResponseCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final UserInfoRepository userInfoRepository;
    private final GalleryRepository galleryRepository;
    private final LikeRepository likeRepository;
    private final StarRepository starRepository;

    @Value("${s3.img.url}")
    private String imgUrl;

    @Value("${s3.collection.url}")
    private String collectionPath;

    private final S3Uploader s3Uploader;

    /**
     * 컬렉션 기본정보 조회
     * @param userId
     * @return
     */
    @Transactional
    public CollectionRes getCollectionInfo(String userId) {
        CollectionRes collectionRes = new CollectionRes();
        //나의 컬렉션 정보 조회
//        Optional<CollectionEntity> collectionEntity = collectionRepository.getCollectionInfo(userId);
        Optional<UserEntity> user = userInfoRepository.findByUserId(userId);
        user.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //사용자 기본정보 조회 및 설정
        CollectionEntity collectionEntity = user.get().getCollectionEntity();
        if(collectionEntity != null){
            collectionRes.setCollectionId(collectionEntity.getId());
            collectionRes.setArtistName(user.get().getNickName());
            collectionRes.setArtistDescription(collectionEntity.getArtistDescription());
            collectionRes.setProfileImg(collectionEntity.getProfilePath() +  collectionEntity.getProfileName());
            collectionRes.setBackProfileImg(collectionEntity.getBackImgPath() + collectionEntity.getBackImgName());
        }


        //나의 작품개수 조회
        Optional<Integer> myArtCnt = galleryRepository.countByUserEntity(user.get());
        collectionRes.setMyArtCnt(myArtCnt.get());

        //내가 좋아요한 작품 개수 조회
        Optional<Integer> likeArtCnt = likeRepository.countByUserEntity(user.get());
        collectionRes.setLikeArtCnt(likeArtCnt.get());

        return collectionRes;
    }

    /**
     * 좋아요 한 작품 목록 조회
     * @param userId
     * @return
     */
    @Transactional
    public List<LikeArtRes> getLikeArtList(String userId, Pageable pageable){
        List<LikeArtRes> likeArtResList = new ArrayList<>();
        //사용자 정보 조회
        Optional<UserEntity> user = userInfoRepository.findByUserId(userId);
        user.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //좋아요한 작품 목록 조회
        Optional<List<GalleryInfoDto>> galleryInfoDtoList = galleryRepository.getLikeArtList(user.get(), pageable);

        for (GalleryInfoDto dto : galleryInfoDtoList.get()) {
            LikeArtRes likeArtRes = new LikeArtRes().builder()
                    .artId(dto.getArtId())
                    .artName(dto.getArtName())
                    .artistName(dto.getArtist())
                    .artUrl(imgUrl + dto.getArtUrl())
                    .build();

            likeArtResList.add(likeArtRes);
        }

        return likeArtResList;
    }

    /**
     * 콜렉션 등록
     * @param profileImageFile
     * @param backProfileImageFile
     * @param collectionUploadReq
     * @throws IOException
     */
    @Transactional
    public void setCollectionUpload(MultipartFile profileImageFile, MultipartFile backProfileImageFile, CollectionUploadReq collectionUploadReq) throws IOException {
        //사용자 정보 조회
        String userId = collectionUploadReq.getUserId().toUpperCase();
        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //컬렉션 유무 확인
        if(userEntity.get().getCollectionEntity() != null){
            throw new CustomException(COLLECTION_INFO_FOUND);
        }

        //s3업로드 및 이미지 url리턴
        //프로필 이미지
        String collectionProfilePath = "";
        String profileName = "";
        if(profileImageFile != null && !profileImageFile.isEmpty()){
            collectionProfilePath = collectionPath + "/profile";
            String s3ProfileImgUrl = s3Uploader.upload(collectionProfilePath, s3Uploader.getUuidFileName(profileImageFile), profileImageFile);
            //s3에 올라간 이미지는 uuid가 포함되어 있어서 새롭게 추출 필요
            String[] temp = s3ProfileImgUrl.split("/");
            profileName = temp[temp.length-1];
        }

        //배경 프로필 이미지
        String collectionBackProfilePath = "";
        String backProfileName = "";
        if(backProfileImageFile != null && !backProfileImageFile.isEmpty()){
            collectionBackProfilePath = collectionPath + "/backProfile";
            String s3BackProfileImgUrl = s3Uploader.upload(collectionBackProfilePath, s3Uploader.getUuidFileName(profileImageFile), backProfileImageFile);
            //s3에 올라간 이미지는 uuid가 포함되어 있어서 새롭게 추출 필요
            String[] temp = s3BackProfileImgUrl.split("/");
            backProfileName = temp[temp.length-1];
        }

        try{
            //컬렉션 저장
            CollectionEntity collectionEntity = new CollectionEntity();
//            String artistName = collectionUploadReq.getArtistName();
            String artistDescription = collectionUploadReq.getArtistDescription();
            collectionEntity.of(collectionProfilePath, profileName, collectionBackProfilePath, backProfileName, artistDescription, userEntity.get());
            collectionRepository.save(collectionEntity);
        } catch (Exception e) {
            //등록 된 이미지 삭제
            s3Uploader.delete(collectionProfilePath, profileName);
            s3Uploader.delete(collectionBackProfilePath, backProfileName);
        }
    }

    /**
     * 컬렉션 수정
     * @param profileImageFile
     * @param backProfileImageFile
     * @param collectionUpdateReq
     * @throws IOException
     */
    @Transactional
    public void collectionUpdate(MultipartFile profileImageFile, MultipartFile backProfileImageFile, CollectionUpdateReq collectionUpdateReq) throws IOException{
        String userId = collectionUpdateReq.getUserId().toUpperCase();
        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        CollectionEntity collectionEntity = userEntity.get().getCollectionEntity();

        Map<String, String> paramMap = new HashMap<>();
        if(collectionEntity != null){
            paramMap.put("oldProfilePath", collectionEntity.getProfilePath());
            paramMap.put("oldProfileName", collectionEntity.getProfileName());
            paramMap.put("oldBackImgPath", collectionEntity.getBackImgPath());
            paramMap.put("oldBackImgName", collectionEntity.getBackImgName());
        }


        //프로필 이미지 수정
        String collectionProfilePath = "";
        String profileName = "";
        //기존 이미지 삭제 후 신규 이미지 경로 및 이름 설정
        if(profileImageFile != null && !profileImageFile.isEmpty()){
            collectionProfilePath = collectionPath + "/profile";
            profileName = s3Uploader.getUuidFileName(profileImageFile);
        }

        //배경 이미지 수정
        String collectionBackProfilePath = "";
        String backProfileName = "";
        //기존 이미지 삭제 후 신규 이미지 경로 및 이름 설정
        if(backProfileImageFile != null && !backProfileImageFile.isEmpty()){
            collectionBackProfilePath = collectionPath + "/backProfile";
            backProfileName = s3Uploader.getUuidFileName(backProfileImageFile);
        }

        String artistDescription = collectionUpdateReq.getArtistDescription();
        //컬렉션 수정
        if(collectionEntity != null){
            collectionEntity.update(collectionProfilePath, profileName, collectionBackProfilePath, backProfileName, artistDescription);
            collectionRepository.save(collectionEntity);
        }else{
            CollectionEntity collection = new CollectionEntity();
            collection.of(collectionProfilePath, profileName, collectionBackProfilePath, backProfileName, artistDescription, userEntity.get());
            collectionRepository.save(collection);
        }


        //s3 삭제 및 업로드
        try{
            //프로필 이미지 삭제 및 업로드
            if(paramMap.get("oldProfilePath") != null && paramMap.get("oldProfileName") != null){
                s3Uploader.delete(paramMap.get("oldProfilePath"), paramMap.get("oldProfileName"));
            }
            if(profileImageFile != null && !profileImageFile.isEmpty()){
                s3Uploader.upload(collectionProfilePath, profileName, profileImageFile);
            }
            //배경이미지 삭제 및 업로드
            if(paramMap.get("oldBackImgPath") != null && paramMap.get("oldBackImgName") != null){
                s3Uploader.delete(paramMap.get("oldBackImgPath"), paramMap.get("oldBackImgName"));
            }
            if(backProfileImageFile != null && !backProfileImageFile.isEmpty()){
                s3Uploader.upload(collectionBackProfilePath, backProfileName, backProfileImageFile);
            }
        }catch (Exception e) {
            log.info(String.valueOf(e));
            s3Uploader.delete(collectionProfilePath, profileName);
            s3Uploader.delete(collectionBackProfilePath, backProfileName);
        }

    }

    /**
     * 컬렉션 스타 정보 조회
     * @param userId
     * @param pageable
     * @return
     */
    @Transactional
    public List<StarArtRes> getStarArtList(String userId, Pageable pageable){
        List<StarArtRes> starArtResList = new ArrayList<>();
        //사용자 정보 조회
        Optional<UserEntity> user = userInfoRepository.findByUserId(userId);
        user.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<StarEntity> starEntityList = starRepository.findByUserEntityOrderByRgsttDtmDesc(user.get());

        if(starEntityList != null && !starEntityList.isEmpty()){
            for (StarEntity starEntity : starEntityList) {
                StarArtRes starArtRes = new StarArtRes().builder()
                        .artId(starEntity.getGalleryEntity().getId())
                        .artName(starEntity.getArtTitle())
                        .artUrl(imgUrl + starEntity.getFilePath() + starEntity.getFileName())
                        .build();
                starArtResList.add(starArtRes);
            }
        }

        return starArtResList;
    }
}
