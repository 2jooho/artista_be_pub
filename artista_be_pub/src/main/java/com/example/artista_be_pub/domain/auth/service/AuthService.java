package com.artista.main.domain.auth.service;

import NiceID.Check.CPClient;
import com.artista.main.domain.auth.dto.PhoneAuthResultDto;
import com.artista.main.domain.auth.dto.request.JoinReq;
import com.artista.main.domain.auth.dto.request.LoginReq;
import com.artista.main.domain.auth.dto.response.GetSocialOAuthRes;
import com.artista.main.domain.auth.dto.response.LoginRes;
import com.artista.main.domain.user.entity.UserEntity;
import com.artista.main.domain.user.repository.UserInfoRepository;
import com.artista.main.global.config.oauth.Constant;
import com.artista.main.global.config.oauth.KakaoOauth;
import com.artista.main.global.dto.KakaoOAuthToken;
import com.artista.main.global.dto.KakaoUser;
import com.artista.main.global.dto.TokenInfo;
import com.artista.main.global.error.CustomException;
import com.artista.main.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.artista.main.domain.constants.ResponseCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoRepository userInfoRepository;
    private final KakaoOauth kakaoOauth;

    @Value("${s3.img.url}")
    private String imgUrl;

    /**
     * 회원가입
     * @param joinReq
     * @return
     */
    @Transactional
    public void join(JoinReq joinReq) {

        log.info("계정 존재 여부 체크");
        Optional<UserEntity> userEntity = userInfoRepository.findByNameAndPhone(joinReq.getName(), joinReq.getPhone());

        if(userEntity.isPresent()){
            log.info("이미 존재하는 계정");
            throw new CustomException(ALREADY_EXISTS_USER);
        }

        //비밀번호 암호화
        joinReq.encryptPassword(passwordEncoder.encode(joinReq.getPassword()));

        log.info("회원 정보 등록");
        UserEntity user = joinReq.toEntity();
        userInfoRepository.save(user);
    }

    /**
     * 일반 로그인
     * @param loginReq
     * @return
     */
    @Transactional
    public LoginRes login(LoginReq loginReq) {
        LoginRes loginRes = new LoginRes();
        String userId = loginReq.getUserId().toUpperCase();
        boolean loginFlag = false;

        log.info("[{}] 로그인 정보 유효성 체크", userId);
        Optional<UserEntity> userEntity = userInfoRepository.findByUserIdAndLoginDvsn(userId, "B");
        userEntity.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

//        loginFlag = passwordEncoder.matches(loginReq.getPassword(), userEntity.get().getPassword()); //비암호화, 암호화

        log.info("[{}] 마지막 로그인 시점 업데이트", userId);

        if (loginFlag) {
            LocalDateTime nowDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String todayDate = nowDate.format(formatter);
            userEntity.get().lastLoginDtUpdate(todayDate);
        }

        log.info("[{}] 토큰 생성", userId);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("loginType", "basic");
        this.createToken(userId, loginReq.getPassword(), response, paramMap);

        //응답값 설정
        loginRes = userEntity.get().toLoginRes(imgUrl);
        return loginRes;
    }

    /**
     * jwt 토큰 생성
     * @param userId
     * @param password
     * @param response
     * @param paramMap
     */
    public void createToken(String userId, String password, HttpServletResponse response, Map<String, String> paramMap) {
        TokenInfo tokenInfo;
        String loginType = paramMap.getOrDefault("loginType", "");

        if (loginType.equals("oAuth")) {
            tokenInfo = jwtTokenProvider.oAuthCreateToken(userId);
        } else{
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            tokenInfo = jwtTokenProvider.generateToken(authentication);
        }

        //레디스에 리프레시 토큰 저장
//        redisUtil.setExpireValue("refresh" + userId, tokenInfo.getRefreshToken(), ((24 * 7) * 60 * 60 * 1000L));

        /// 헤더에 어세스 토큰 추가
        jwtTokenProvider.setHeaderAccessToken(response, tokenInfo.getAccessToken());
        jwtTokenProvider.setHeaderRefreshToken(response, tokenInfo.getRefreshToken());
    }

    /**
     * 소셜 로그인 요청
     * @param socialLoginType
     * @throws IOException
     */
    public void oAuthRequest(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL = ""; //리다이렉트 URL
        switch (socialLoginType){
            case KAKAO:{
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = kakaoOauth.getOauthRedirectURL();
            }break;
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }

        response.sendRedirect(redirectURL);
    }

    /**
     * 소셜 로그인 콜백 인증
     * @param socialLoginType
     * @param code
     * @return
     */
    @Transactional
    public GetSocialOAuthRes oAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws IOException{
        GetSocialOAuthRes getSocialOAuthRes;
        if (code.isBlank()) throw new CustomException(INVALID_PARAMETER);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("loginType", "oAuth");

        switch (socialLoginType){
            case KAKAO:{
                //카카오로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);

                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                KakaoOAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);

                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);

                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                KakaoUser kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);

                //카카오에게 전달받은 이메일
                String userId = kakaoUser.getKakao_account().getEmail().toUpperCase();

                //db에 아이디가 존재하는지 확인
                Optional<UserEntity> userEntity = userInfoRepository.findByUserIdAndLoginDvsn(userId, "K");

                LocalDateTime nowDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String todayDate = nowDate.format(formatter);

                if(userEntity.isPresent()){
                    //로그인 시점 업데이트
                    userEntity.get().lastLoginDtUpdate(todayDate);
                    Boolean isStar = userEntity.get().getStarEntityList().size() > 0 ? true : false;
                    getSocialOAuthRes = new GetSocialOAuthRes(userId, oAuthToken.getAccess_token(), oAuthToken.getToken_type(),
                            userEntity.get().getNickName(), imgUrl + userEntity.get().getCollectionEntity().getProfilePath() + userEntity.get().getCollectionEntity().getProfileName(),
                            userEntity.get().getCollectionEntity().getArtistInstaAddr(), isStar);
                } else{
                    //회원 등록
                    UserEntity user = new UserEntity();
                    String agree = "Y:N:N";
                    String birth = kakaoUser.getKakao_account().getBirthyear() + kakaoUser.getKakao_account().getBirthday();
                    String password = passwordEncoder.encode(userId+"kakao!@@#");
                    user.oAuthJoin(userId, password, "", birth, userId, agree, todayDate, "K");
                    userInfoRepository.save(user);

                    getSocialOAuthRes = new GetSocialOAuthRes(userId, oAuthToken.getAccess_token(), oAuthToken.getToken_type(), null, null, null, null);
                }

                //jwt 토큰 생성 후 헤더 응답
                this.createToken(userId, "", response, paramMap);

                // json 응답 설정
                return getSocialOAuthRes;

            }
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
    }

    /**
     * PASS 인증
     */
    @Transactional
    public String passAuth(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        CPClient niceCheck = new CPClient();

        String sSiteCode = "CC475";            // NICE로부터 부여받은 사이트 코드
        String sSitePassword = "lidXA9hpCIIQ";        // NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = niceCheck.getRequestNO(sSiteCode);        // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로

        String sAuthType = "";        // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun = "N";        //Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize = "";        // 없으면 기본 웹페이지 / Mobile : 모바일페이지
        String sGender = "";            // 없으면 기본 선택 값, 0 : 여자, 1 : 남자

        String sReturnUrl = "http://localhost:8082/aa/auth/pass/success"; // 성공시 이동될 URL
        String sErrorUrl = "http://localhost:8082/aa/auth/pass/fail"; // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
        if (iReturn == 0) {
            sEncData = niceCheck.getCipherData();
        } else if (iReturn == -1) {
            sMessage = "암호화 시스템 에러입니다.";
        } else if (iReturn == -2) {
            sMessage = "암호화 처리오류입니다.";
        } else if (iReturn == -3) {
            sMessage = "암호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        request.getSession().setAttribute("REQ_SEQ", sRequestNumber);

        if(iReturn != 0){
            log.info(sMessage);
        }

//        modelMap.addAttribute("sMessage", sMessage);
//        modelMap.addAttribute("sEncData", sEncData);

        return sEncData;
    }

    /**
     * pass 인증 성공
     * @return
     */
    public RedirectView passSuccess(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        PhoneAuthResultDto phoneAuthResultDto = new PhoneAuthResultDto();
        CPClient niceCheck = new CPClient();

        String sEncodeData = request.getParameter("EncodeData");

        String sSiteCode = "CC475";            // NICE로부터 부여받은 사이트 코드
        String sSitePassword = "lidXA9hpCIIQ";        // NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";            // 복호화한 시간
        String sRequestNumber = "";            // 요청 번호
        String sResponseNumber = "";        // 인증 고유번호
        String sAuthType = "";                // 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

        if (iReturn == 0) {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            HashMap mapresult = niceCheck.fnParse(sPlainData);
            sRequestNumber = (String) mapresult.get("REQ_SEQ");
                            sResponseNumber = (String) mapresult.get("RES_SEQ");
            sAuthType = (String) mapresult.get("AUTH_TYPE");

            String session_sRequestNumber = (String) request.getSession().getAttribute("REQ_SEQ");
            if (!sRequestNumber.equals(session_sRequestNumber)) {
                sMessage = "세션값 불일치 오류입니다.";
            }

            phoneAuthResultDto = PhoneAuthResultDto.builder()
                    .sRequestNumber(sRequestNumber)
                    .sResponseNumber(sResponseNumber)
                    .sAuthType(sAuthType)
                    .sCipherTime(sCipherTime)
                    .name((String) mapresult.get("NAME"))
                    .birth((String) mapresult.get("BIRTHDATE"))
                    .gender((String) mapresult.get("GENDER"))
                    .dupInfo((String) mapresult.get("DI"))
                    .connInfo((String) mapresult.get("CI"))
                    .phone((String) mapresult.get("MOBILE_NO"))
                    .mobileCompany((String) mapresult.get("MOBILE_CO"))
                    .build();

            modelMap.addAttribute("dto", phoneAuthResultDto);

        } else if (iReturn == -1) {
            sMessage = "복호화 시스템 오류입니다.";
        } else if (iReturn == -4) {
            sMessage = "복호화 처리 오류입니다.";
        } else if (iReturn == -5) {
            sMessage = "복호화 해쉬 오류입니다.";
        } else if (iReturn == -6) {
            sMessage = "복호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else if (iReturn == -12) {
            sMessage = "사이트 패스워드 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }
        modelMap.addAttribute("sMessage", sMessage);

        RedirectView redirectView = new RedirectView();
//        redirectView.addStaticAttribute("dto", phoneAuthResultDto);
//        + URLEncoder.encode(phoneAuthResultDto.toString(), StandardCharsets.UTF_8)
        String name = URLEncoder.encode(phoneAuthResultDto.getName(), StandardCharsets.UTF_8);
        String birth = URLEncoder.encode(phoneAuthResultDto.getBirth(), StandardCharsets.UTF_8);
        String phone = URLEncoder.encode(phoneAuthResultDto.getPhone(), StandardCharsets.UTF_8);
        birth = birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6);
        phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
        redirectView.setUrl("http://localhost:3000/PassSuccess/callback?name="+name+"&birth="+birth+"&phone="+phone);

        return redirectView;
//        return phoneAuthResultDto;
//        return "/pass/success.html";
    }


    /**
     * pass 성공 callback
     * @param encodeData
     * @return
     */
//    @Transactional
//    public PhoneAuthResultDto passSuccessCallback(String encodeData){
//        PhoneAuthResultDto phoneAuthResultDto = new PhoneAuthResultDto();
//        CPClient niceCheck = new CPClient();
//
//        String sEncodeData = encodeData;
////        String sEncodeData = request.getParameter("EncodeData");
//
//        String sSiteCode = "CC475";            // NICE로부터 부여받은 사이트 코드
//        String sSitePassword = "lidXA9hpCIIQ";        // NICE로부터 부여받은 사이트 패스워드
//
//        String sCipherTime = "";            // 복호화한 시간
//        String sRequestNumber = "";            // 요청 번호
//        String sResponseNumber = "";        // 인증 고유번호
//        String sAuthType = "";                // 인증 수단
//        String sMessage = "";
//        String sPlainData = "";
//
//        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
//
//        if (iReturn == 0) {
//            sPlainData = niceCheck.getPlainData();
//            sCipherTime = niceCheck.getCipherDateTime();
//
//            // 데이타를 추출합니다.
//            HashMap mapresult = niceCheck.fnParse(sPlainData);
//            sRequestNumber = (String) mapresult.get("REQ_SEQ");
//            sResponseNumber = (String) mapresult.get("RES_SEQ");
//            sAuthType = (String) mapresult.get("AUTH_TYPE");
//
//            String session_sRequestNumber = (String) request.getSession().getAttribute("REQ_SEQ");
//            if (!sRequestNumber.equals(session_sRequestNumber)) {
//                sMessage = "세션값 불일치 오류입니다.";
//            }
//
//            phoneAuthResultDto = PhoneAuthResultDto.builder()
//                    .sRequestNumber(sRequestNumber)
//                    .sResponseNumber(sResponseNumber)
//                    .sAuthType(sAuthType)
//                    .sCipherTime(sCipherTime)
//                    .name((String) mapresult.get("NAME"))
//                    .birth((String) mapresult.get("BIRTHDATE"))
//                    .gender((String) mapresult.get("GENDER"))
//                    .dupInfo((String) mapresult.get("DI"))
//                    .connInfo((String) mapresult.get("CI"))
//                    .phone((String) mapresult.get("MOBILE_NO"))
//                    .mobileCompany((String) mapresult.get("MOBILE_CO"))
//                    .build();
//
//        } else if (iReturn == -1) {
//            sMessage = "복호화 시스템 오류입니다.";
//        } else if (iReturn == -4) {
//            sMessage = "복호화 처리 오류입니다.";
//        } else if (iReturn == -5) {
//            sMessage = "복호화 해쉬 오류입니다.";
//        } else if (iReturn == -6) {
//            sMessage = "복호화 데이터 오류입니다.";
//        } else if (iReturn == -9) {
//            sMessage = "입력 데이터 오류입니다.";
//        } else if (iReturn == -12) {
//            sMessage = "사이트 패스워드 오류입니다.";
//        } else {
//            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
//        }
//
//        return phoneAuthResultDto;
//    }

    /**
     * pass 실패
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @Transactional
    public String passFail(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        CPClient niceCheck = new CPClient();

        String sEncodeData = request.getParameter("EncodeData");

        String sSiteCode = "CC475";            // NICE로부터 부여받은 사이트 코드
        String sSitePassword = "lidXA9hpCIIQ";        // NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";            // 복호화한 시간
        String sRequestNumber = "";            // 요청 번호
        String sResponseNumber = "";        // 인증 고유번호
        String sAuthType = "";                // 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

        if (iReturn == 0) {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            HashMap mapresult = niceCheck.fnParse(sPlainData);
            sRequestNumber = (String) mapresult.get("REQ_SEQ");
            sResponseNumber = (String) mapresult.get("RES_SEQ");
            sAuthType = (String) mapresult.get("AUTH_TYPE");

            PhoneAuthResultDto phoneAuthResultDto = PhoneAuthResultDto.builder()
                    .sRequestNumber(sRequestNumber)
                    .sResponseNumber(sResponseNumber)
                    .sAuthType(sAuthType)
                    .sCipherTime(sCipherTime)
                    .build();

            modelMap.addAttribute("dto", phoneAuthResultDto);

        } else if (iReturn == -1) {
            sMessage = "복호화 시스템 오류입니다.";
        } else if (iReturn == -4) {
            sMessage = "복호화 처리 오류입니다.";
        } else if (iReturn == -5) {
            sMessage = "복호화 해쉬 오류입니다.";
        } else if (iReturn == -6) {
            sMessage = "복호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else if (iReturn == -12) {
            sMessage = "사이트 패스워드 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }
        modelMap.addAttribute("sMessage", sMessage);

        return "/pass/fail.html";
    }
}
