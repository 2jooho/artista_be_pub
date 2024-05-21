package com.artista.main.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class FindIdReq {
    /**
     * 이름
     */
    @NotBlank(message = "name은 필수 입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$" , message = "이름은 특수문자를 포함하지 않은 2~20자리여야 합니다.")
    private String name;

    /**
     * 핸드폰 번호
     */
    @NotBlank(message = "phone는 필수 입니다.")
    @Pattern(regexp = "^[0-9]{0,12}$" , message = "핸드폰번호는 12자리 숫자 형식입니다.")
    private String phone;

    /**
     * 핸드폰 인증 여부
     */
    @NotBlank(message = "인증여부는 필수 입니다.")
    @Pattern(regexp = "^[Y,N]{1}$" , message = "이름은 특수문자를 포함하지 않은 2~20자리여야 합니다.")
    private String phoneAuthYn;
}
