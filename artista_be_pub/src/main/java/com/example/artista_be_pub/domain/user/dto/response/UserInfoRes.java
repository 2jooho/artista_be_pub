package com.artista.main.domain.user.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRes {
    /** 이름 */
    private String name;

    /** 핸드폰 번호 */
    private String phone;

    /** 이메일 */
    private String email;

    /** 인스타 주소 */
    private String instaId;

    /** 생년월일 */
    private String birth;

}
