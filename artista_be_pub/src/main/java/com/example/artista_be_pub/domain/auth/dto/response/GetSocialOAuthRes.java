package com.artista.main.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSocialOAuthRes {
    /**
     * 사용자 id
     */
    private String userId;

    /**
     * 액세스 토큰
     */
    private String accessToken; // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰

    /**
     * 토큰 타입
     */
    private String tokenType;   // 반환된 토큰 유형(Bearer 고정)

    /** 닉네임 */
    private String nickname;

    /** 프로필 URL */
    private String profileUrl;

    /** 인스타 주소 */
    private String instaId;

    /** 스타 여부 */
    private Boolean isStar;

}