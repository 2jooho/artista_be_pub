package com.artista.main.domain.user.entity;

import com.artista.main.domain.auth.dto.response.LoginRes;
import com.artista.main.domain.collection.entity.CollectionEntity;
import com.artista.main.domain.gallery.entity.GalleryEntity;
import com.artista.main.domain.star.entity.StarEntity;
import com.artista.main.domain.user.dto.response.UserInfoRes;
import com.artista.main.global.entity.BaseEntity;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "art_user_info")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 아이디 */
    @Column(nullable = false, unique = true, length = 20)
    private String userId;

    /** 비밀번호 */
    @Column(nullable = false)
    private String password;

    /** 이름 */
    @Column(nullable = true, length = 20)
    private String name;

    /** 닉네임 */
    @Column(nullable = true, length = 20)
    private String nickName;

    /** 생년월일 */
    @Column(nullable = true, length = 8)
    private String birth;

    /** 핸드폰 번호 */
    @Column(nullable = true, length = 15)
    private String phone;

    /** 이메일 */
    @Column(nullable = true, length = 100)
    private String email;

    /** 주소 */
    @Column(nullable = true)
    private String address;

    /** 약관 동의(Y:동의, N:미동의) */
    @Column(nullable = false, length = 10)
    private String agree;

    /** 마지막 로그인 일시 */
    @Column(length = 14)
    private String lastLoginDt;

    /**
     * 로그인 구분(B: 기본, K: 카카오)
     */
    @Column(length = 2)
    private String loginDvsn;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GalleryEntity> galleryEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CollectionEntity collectionEntity = new CollectionEntity();

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StarEntity> starEntityList = new ArrayList<>();

    /**
     * 마지막 로그인 날짜 업데이트
     * @param lastLoginDt
     */
    public void lastLoginDtUpdate(String lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    /**
     * 일반 로그인 응답 설정
     * @return
     */
    public LoginRes toLoginRes(String imgUrl) {
        String profileUrl = "";
        String instaId = "";
        if(collectionEntity != null){
            profileUrl = collectionEntity.getProfilePath() + collectionEntity.getProfileName();
            instaId = collectionEntity.getArtistInstaAddr();
        }

        Boolean isStar = starEntityList.size() > 0 ? true : false;
        LoginRes loginRes = LoginRes.builder()
                .userId(userId)
                .nickname(nickName)
                .profileUrl(imgUrl + profileUrl)
                .instaId(instaId)
                .isStar(isStar)
                .build();

        return loginRes;
    }

    public UserInfoRes toUserRes() {
        String instaId = "";
        if(collectionEntity != null){
            instaId = collectionEntity.getArtistInstaAddr();
        }

        UserInfoRes userInfoRes = UserInfoRes.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .instaId(instaId)
                .birth(birth)
                .build();

        return userInfoRes;
    }

    /**
     * 비밀번호 재설정
     * @param password
     */
    public void resetPassword(String password){
        this.password = password;
    }

    public void oAuthJoin(String userId, String password, String name, String birth, String email, String agree, String lastLoginDt, String loginDvsn){
        this.userId = userId;
        this.password = this.encryptPassword(password);
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.agree = agree;
        this.lastLoginDt = lastLoginDt;
        this.loginDvsn = loginDvsn;
    }

    public String encryptPassword(String BCryptpassword) {
        return BCryptpassword;
    }

    public void update(String password, String birth, String email, String nickName, String phone, String agree){
        if(!password.isBlank()){
            this.password = password;
        }
        if(!birth.isBlank()){
            this.birth = birth;
        }
        if(!email.isBlank()){
            this.email = email;
        }
        if(!nickName.isBlank()){
            this.nickName = nickName;
        }
        if(!phone.isBlank()){
            this.phone = phone;
        }
        if(!agree.isBlank()){
            this.agree = agree;
        }
    }

}
