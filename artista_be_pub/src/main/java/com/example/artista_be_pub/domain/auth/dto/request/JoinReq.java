package com.artista.main.domain.auth.dto.request;

import com.artista.main.domain.user.entity.UserEntity;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class JoinReq {
    /**
     * 아이디
     */
    @NotBlank(message = "userId는 필수 입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "아이디는 2~20자리 영문, 한굴, 숫자 조합이여야 합니다.")
    private String userId;

    /**
     * 비밀번호
     */
    @NotBlank(message = "password 필수 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    /**
     * 이름
     */
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$" , message = "이름은 특수문자를 포함하지 않은 2~20자리여야 합니다.")
    private String name;

    /**
     * 닉네임
     */
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ]{2,20}$" , message = "닉네임은 특수문자를 포함하지 않은 2~20자리여야 합니다.")
    private String nickName;

    /**
     * 생년월일
     */
    @NotBlank(message = "생년월일은 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]{8}$" , message = "생년월일은 8자리 숫자입니다.")
    private String birth;

    /**
     * 핸드폰 번호
     */
    @NotBlank(message = "핸드폰번호는 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]{0,12}$" , message = "핸드폰번호는 최대 12자리 숫자 형식입니다.")
    private String phone;


    /**
     * 이메일
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    /**
     * 약관 동의 여부(필수/마케팅/개인정보)
     * ex) Y:N:Y
     */
    @NotBlank(message = "약관 동의 여부는 필수입니다.")
    private String agree;

//    /**
//     * 주소
//     */
//    private String address;


    /**
     * 요청받은 패스워드 암호화
     * @param BCryptpassword
     */
    public void encryptPassword(String BCryptpassword) {
        this.password = BCryptpassword;
    }

    public UserEntity toEntity() {
        UserEntity user = UserEntity.builder()
                .userId(userId.toUpperCase())
                .password(password)
                .nickName(nickName.toUpperCase())
                .name(name)
                .birth(birth)
                .phone(phone)
                .email(email.toUpperCase())
                .agree(agree)
                .loginDvsn("B")
                .build();

        return user;
    }

}
