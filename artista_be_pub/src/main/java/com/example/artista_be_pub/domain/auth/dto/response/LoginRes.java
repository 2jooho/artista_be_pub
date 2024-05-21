package com.artista.main.domain.auth.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {
    /** 아이디 */
    private String userId;

    /** 닉네임 */
    private String nickname;

    /** 프로필 URL */
    private String profileUrl;

    /** 인스타 주소 */
    private String instaId;

    /** 스타 여부 */
    private Boolean isStar;

}
