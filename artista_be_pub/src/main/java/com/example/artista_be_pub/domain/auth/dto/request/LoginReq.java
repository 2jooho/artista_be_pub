package com.artista.main.domain.auth.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class LoginReq {
    /**
     * 아이디
     */
    @NotBlank(message = "userId는 필수 입니다.")
    @Size(max = 20)
    @ApiModelProperty(name="userId", example = "test", value = "사용자 아이디", required = true) //swagger 처리
    private String userId;

    /**
     * 비밀번호
     */
    @NotBlank(message = "password 필수 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
}
