package com.artista.main.global.config.oauth;


import com.artista.main.global.dto.KakaoOAuthToken;
import com.artista.main.global.dto.KakaoUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth {
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    private final ObjectMapper objectMapper;
    @Autowired
    private final RestTemplate restTemplate;

    /**
     * 소셜로그인 리다이렉트 url
     * @return
     */
    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", KAKAO_CLIENT_ID);
        params.put("redirect_uri", KAKAO_REDIRECT_URL);

        //parameter를 형식에 맞춰 구성해주는 함수
        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        String redirectURL = KAKAO_AUTH_URI + "/oauth/authorize" + "?" + paramStr;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        String TOKEN_REQUEST_URL = KAKAO_AUTH_URI + "/oauth/token";
        RestTemplate restTemplate=new RestTemplate();

//        Map<String, Object> params = new HashMap<>();
//        params.put("grant_type", "authorization_code");
//        params.put("client_id", KAKAO_CLIENT_ID);
//        params.put("client_secret", KAKAO_CLIENT_SECRET);
//        params.put("code", code);
//        params.put("redirect_uri", KAKAO_REDIRECT_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type"   , "authorization_code");
        params.add("client_id"    , KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("code"         , code);
        params.add("redirect_uri" , KAKAO_REDIRECT_URL);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(TOKEN_REQUEST_URL , params, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity;
        }

        return null;
    }

    public KakaoOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() = " + response.getBody());

        KakaoOAuthToken kakaoOAuthToken = objectMapper.readValue(response.getBody(), KakaoOAuthToken.class);

        return kakaoOAuthToken;
    }

    public ResponseEntity<String> requestUserInfo(KakaoOAuthToken oAuthToken) {
        String USERINFO_REQUEST_URL = KAKAO_API_URI + "/v2/user/me";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(USERINFO_REQUEST_URL, HttpMethod.POST, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    public KakaoUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        KakaoUser kakaoUser = objectMapper.readValue(userInfoRes.getBody(), KakaoUser.class);

//        LinkedHashMap<String, String> kakaoUserMap = objectMapper.readValue(userInfoRes.getBody(), LinkedHashMap.class);
//        Map<String, String> kakaoAccountMap = objectMapper.readValue(kakaoUserMap.get("kakao_account"), Map.class);
//        kakaoUser.setId(kakaoUserMap.get("id"));
//        kakaoUser.setBirthday(kakaoAccountMap.get("birthyear") + kakaoAccountMap.get("birthday"));
//        kakaoUser.setEmail(kakaoAccountMap.get("email"));

        return kakaoUser;
    }
}