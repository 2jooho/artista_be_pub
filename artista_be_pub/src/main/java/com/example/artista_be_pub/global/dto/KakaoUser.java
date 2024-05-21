package com.artista.main.global.dto;

import lombok.*;

// 액세스 토큰을 보내 받아올 카카오에 등록된 사용자 정보
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoUser {
    public Long id;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public class Properties { //(1)
        public String nickname;
        public String profile_image; // 이미지 경로 필드1
        public String thumbnail_image;
    }

    @Data
    public class KakaoAccount { //(2)
        public String email;
        public String birthyear;
        public String birthday;

        @Data
        public class Profile {
            public String nickname;
        }
    }

}