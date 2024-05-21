package com.artista.main.domain.user.dto.request;

import com.artista.main.domain.user.entity.UserEntity;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UserInfoReq {
    /**
     * 아이디
     */
    @NotBlank(message = "userId는 필수 입니다.")
    private String userId;

    /**
     * 비밀번호
     */
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    /**
     * 닉네임
     */
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ]{2,20}$" , message = "닉네임은 특수문자를 포함하지 않은 2~20자리여야 합니다.")
    private String nickName;

    /**
     * 생년월일
     */
    @Pattern(regexp = "^[0-9]{8}$" , message = "생년월일은 8자리 숫자입니다.")
    private String birth;

    /**
     * 핸드폰 번호
     */
    @Pattern(regexp = "^[0-9]{0,12}$" , message = "핸드폰번호는 최대 12자리 숫자 형식입니다.")
    private String phone;

    /**
     * 이메일
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    /**
     * 약관 동의 여부(필수/마케팅/개인정보)
     * ex) Y:N:Y
     */
    private String agree;

    /**
     * 로그인 타입(B:기본, K:카카오)
     */
    private String loginType;

    /**
     * 요청받은 패스워드 암호화
     * @param BCryptpassword
     */
    public void encryptPassword(String BCryptpassword) {
        this.password = BCryptpassword;
    }

    public void toEntity(UserEntity userEntity) {
        userEntity.update(password, birth, email.toUpperCase(), nickName.toUpperCase(), phone, agree);
    }

}
