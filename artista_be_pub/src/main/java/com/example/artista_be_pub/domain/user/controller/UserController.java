package com.artista.main.domain.user.controller;

import com.artista.main.domain.constants.BaseController;
import com.artista.main.domain.user.dto.request.FindIdReq;
import com.artista.main.domain.user.dto.request.ResetPwReq;
import com.artista.main.domain.user.dto.request.UserInfoReq;
import com.artista.main.domain.user.dto.response.FindIdRes;
import com.artista.main.domain.user.dto.response.UserInfoRes;
import com.artista.main.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.artista.main.domain.constants.ApiUrl.*;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 로그인 중복 체크
     * @param userId
     * @return
     */
    @GetMapping(ID_CHECK_URL)
    public ResponseEntity idCheck(@RequestParam String userId){
        userService.idCheck(userId.toUpperCase());

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 닉네임 중복 체크
     * @param nickName
     * @return
     */
    @GetMapping(NICKNAME_CHECK_URL)
    public ResponseEntity nickNameCheck(@RequestParam String nickName){
        userService.nickNameCheck(nickName.toUpperCase());

        return new ResponseEntity(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 아이디 찾기
     * @param findIdReq
     * @return
     */
    @PostMapping(FIND_ID_URL)
    public ResponseEntity<FindIdRes> findUserId(@Valid @RequestBody FindIdReq findIdReq){
        FindIdRes findIdRes = userService.findUserId(findIdReq);

        return new ResponseEntity<>(findIdRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 비밀번호 재설정
     * @return
     */
    @PostMapping(REST_PW_URL)
    public ResponseEntity resetPwUserInfo(@Valid @RequestBody ResetPwReq resetPwReq) {
        userService.resetPw(resetPwReq);

        return new ResponseEntity(getSuccessHeaders(),HttpStatus.OK);
    }

    /**
     * 개인정보 수정
     * @param userInfoReq
     * @return
     */
    @PostMapping(USER_UPDATE_URL)
    public ResponseEntity userInfoUpdate(@Valid @RequestBody UserInfoReq userInfoReq){

        userService.userInfoUpdate(userInfoReq);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 비밀번호 체크
     * @param userInfoReq
     * @return
     */
    @PostMapping(REST_PW_CHECK_URL)
    public ResponseEntity<UserInfoRes> userPwCheck(@Valid @RequestBody UserInfoReq userInfoReq){

        UserInfoRes userInfoRes = userService.userPwCheck(userInfoReq);
        return new ResponseEntity<UserInfoRes>(userInfoRes, getSuccessHeaders(), HttpStatus.OK);
    }

}
