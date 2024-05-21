package com.artista.main.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//구글에 일회성 코드를 다시 보내 받아올 액세스 토큰을 포함한 JSON 문자열을 담을 클래스
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KakaoOAuthToken {
    private String access_token;    // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    private String token_type;  // 반환된 토큰 유형(Bearer 고정)
    private String refresh_token;    // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰

}