package com.artista.main.domain.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class ResetPwReq {
    /**
     * 아이디
     */
    @NotBlank(message = "userId는 필수 입니다.")
    @Size(max = 20)
    private String userId;

    /**
     * 비밀번호
     */
    @NotBlank(message = "newPassword 필수 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String newPassword;

    /**
     * 비밀번호 확인
     */
    @NotBlank(message = "newPasswordCheck 필수 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String newPasswordCheck;

    /**
     * 핸드폰 인증 여부
     */
    @NotBlank(message = "인증여부는 필수 입니다.")
    @Pattern(regexp = "^[Y,N]{1}$" , message = "이름은 특수문자를 포함하지 않은 2~20자리여야 합니다.")
    private String phoneAuthYn;
}
