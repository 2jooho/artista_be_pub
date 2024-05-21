package com.artista.main.domain.user.service;

import com.artista.main.domain.auth.dto.request.JoinReq;
import com.artista.main.domain.user.dto.request.FindIdReq;
import com.artista.main.domain.user.dto.request.ResetPwReq;
import com.artista.main.domain.user.dto.request.UserInfoReq;
import com.artista.main.domain.user.dto.response.FindIdRes;
import com.artista.main.domain.user.dto.response.UserInfoRes;
import com.artista.main.domain.user.entity.UserEntity;
import com.artista.main.domain.user.repository.UserInfoRepository;
import com.artista.main.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.artista.main.domain.constants.ResponseCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 아이디 중복 체크
     * @param userId
     */
    @Transactional
    public void idCheck(String userId){
        //id validation
        if(userId.isBlank() || userId.length() > 20){
            throw new CustomException(VALIDATION_FAIL);
        }
        Optional<Integer> userCount = userInfoRepository.countByUserIdAndLoginDvsn(userId, "B");
        //아이디 존재
        if(userCount.isPresent() && userCount.get() > 0) {
            throw new CustomException(ALREADY_EXISTS_USER);
        }
    }

    /**
     * 닉네임 중복검사
     * @param nickName
     */
    @Transactional
    public void nickNameCheck(String nickName){
        //nickName validation
        if(nickName.isBlank() || nickName.length() > 20){
            throw new CustomException(VALIDATION_FAIL);
        }
        Optional<Integer> nickNameCount = userInfoRepository.countByNickName(nickName);
        //아이디 존재
        if(nickNameCount.isPresent() && nickNameCount.get() > 0) {
            throw new CustomException(ALREADY_EXISTS_NICKNAME);
        }
    }

    /**
     * 아이디 찾기
     * @return
     */
    @Transactional
    public FindIdRes findUserId(FindIdReq findIdReq) {
        FindIdRes findIdRes = new FindIdRes();
        if(!findIdReq.getPhoneAuthYn().equals("Y")){
            throw new CustomException(INVALID_REQUEST);
        }
        Optional<UserEntity> userEntity = userInfoRepository.findByNameAndPhone(findIdReq.getName(), findIdReq.getPhone());
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        findIdRes.setUserId(userEntity.get().getUserId());

        return findIdRes;
    }

    /**
     * 비밀번호 재설정
     */
    @Transactional
    public void resetPw(ResetPwReq resetPwReq) {
        String userId = resetPwReq.getUserId().toUpperCase();

        //신규 비밀번호 및 신규 비밀번호 확인 비교
        if(!resetPwReq.getNewPassword().equals(resetPwReq.getNewPasswordCheck())){
            throw new CustomException(RESET_PASSWORD_MATCH_FAIL);
        }

        //인증여부 validation
        if(!resetPwReq.getPhoneAuthYn().equals("Y")){
            throw new CustomException(INVALID_REQUEST);
        }

        //회원정보 조회
        Optional<UserEntity> userEntity = userInfoRepository.findByUserIdAndLoginDvsn(userId, "B");
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //비밀번호 업데이트
        userEntity.get().resetPassword(passwordEncoder.encode(resetPwReq.getNewPassword()));
    }

    /**
     * 사용자 정보 업데이트
     * @param userInfoReq
     */
    @Transactional
    public void userInfoUpdate(UserInfoReq userInfoReq){
        String userId = userInfoReq.getUserId().toUpperCase();

        //사용자 정보 조회
        Optional<UserEntity> userEntity = userInfoRepository.findByUserId(userId);
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //비밀번호 존재 시 비밀번호 암호화 처리
        if(!userInfoReq.getPassword().isBlank()){
            userInfoReq.encryptPassword(passwordEncoder.encode(userInfoReq.getPassword()));
        }

        //조회된 사용자 정보 업데이트
        userInfoReq.toEntity(userEntity.get());
    }

    /**
     * 비밀번호 체크
     * @param userInfoReq
     * @return
     */
    @Transactional
    public UserInfoRes userPwCheck(UserInfoReq userInfoReq){
        UserInfoRes userInfoRes;
        String loginType = userInfoReq.getLoginType() != null ? userInfoReq.getLoginType() : "B";

        Optional<UserEntity> userEntity = userInfoRepository.findByUserIdAndLoginDvsn(userInfoReq.getUserId().toUpperCase(), loginType.toUpperCase());
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //비밀번호 체크
        if(!passwordEncoder.matches(userInfoReq.getPassword(), userEntity.get().getPassword())){
            throw new CustomException(USER_NOT_FOUND);
        }

        userInfoRes = userEntity.get().toUserRes();

        return userInfoRes;
    }

}
