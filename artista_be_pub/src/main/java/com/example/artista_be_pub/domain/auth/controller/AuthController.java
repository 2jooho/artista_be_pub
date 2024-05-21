package com.artista.main.domain.auth.controller;

import com.artista.main.domain.auth.dto.PhoneAuthResultDto;
import com.artista.main.domain.auth.dto.request.JoinReq;
import com.artista.main.domain.auth.dto.request.LoginReq;
import com.artista.main.domain.auth.dto.response.GetSocialOAuthRes;
import com.artista.main.domain.auth.dto.response.LoginRes;
import com.artista.main.domain.auth.service.AuthService;
import com.artista.main.domain.constants.BaseController;
import com.artista.main.global.config.oauth.Constant;
import com.artista.main.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

import static com.artista.main.domain.constants.ApiUrl.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Slf4j
public class AuthController extends BaseController {
    @Autowired
    private AuthService authService;

    /**
     * 일반 로그인
     * @param loginReq
     * @return
     */
    @PostMapping(LOGIN_URL)
    public ResponseEntity<LoginRes> login(@Valid @RequestBody LoginReq loginReq){

        LoginRes loginRes = authService.login(loginReq);
        return new ResponseEntity<>(loginRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 회원가입
     * @param joinReq
     * @return
     */
    @PostMapping(JOIN_URL)
    public ResponseEntity join(@Valid @RequestBody JoinReq joinReq){

        authService.join(joinReq);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 소셜 로그인으로 리다이렉트(1-1)
     * @param socialLoginPath
     * @throws IOException
     */
    @GetMapping(AUTH_TYPE_LOGIN_URL)
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String socialLoginPath) throws IOException {
        //소셜 로그인 타입
        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        authService.oAuthRequest(socialLoginType);
    }

    /**
     * 소셜 로그인으로 콜백 후 로그인 처리(1-2)
     * @param socialLoginPath
     * @param authCode
     * @return
     * @throws IOException
     * @throws CustomException
     */
    @GetMapping(value = AUTH_CALLBACK_URL)
    public ResponseEntity<GetSocialOAuthRes> callback (
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String authCode) throws IOException, CustomException {

        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = authService.oAuthLogin(socialLoginType, authCode);

        return new ResponseEntity<>(getSocialOAuthRes, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * pass 메인 페이지
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @GetMapping(value = AUTH_PASS_URL)
    public ResponseEntity<String> getPassPage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String url = authService.passAuth(request, response, modelMap);

        return new ResponseEntity<>(url, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * pass 성공 페이지
     * @return
     */
    @RequestMapping(value = AUTH_PASS_SUCCESS_URL, method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView passSuccess(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){

//        PhoneAuthResultDto dto = authService.passSuccess(request, response, modelMap);
//        String url = authService.passSuccess(request, response, modelMap);
        RedirectView url = authService.passSuccess(request, response, modelMap);
//        return new ResponseEntity<>(dto, getSuccessHeaders(), HttpStatus.OK);
        return url;
    }

//    @RequestMapping(value = AUTH_PASS_SUCCESS_CALLBACK_URL, method = {RequestMethod.GET, RequestMethod.POST})
//    public ResponseEntity<PhoneAuthResultDto> passSuccessCallback(@RequestParam(name = "code") String encodeData){
//
//        PhoneAuthResultDto dto = authService.passSuccessCallback(encodeData);
//
//        return new ResponseEntity<>(dto, getSuccessHeaders(), HttpStatus.OK);
//    }

    @RequestMapping(value = AUTH_PASS_FAIL_URL, method = {RequestMethod.GET, RequestMethod.POST})
    public String passFail(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String url = authService.passFail(request, response, modelMap);

        return url;
    }
}
