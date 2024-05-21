package com.artista.main.domain.collection.entity;

import com.artista.main.domain.user.entity.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ART_COLLECTION")
@DynamicUpdate
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 프로필 이미지 경로 */
    @Column(length = 255)
    private String profilePath;

    /** 프로필 이미지 명 */
    @Column(length = 255)
    private String profileName;

    /** 배경 이미지 경로 */
    @Column(length = 255)
    private String backImgPath;

    /** 배경 이미지 명 */
    @Column(length = 255)
    private String backImgName;

    /**
     * 작가설명
     */
    @Column(length = 200)
    private String artistDescription;

    /**
     * 작가 인스타 주소
     */
    @Column(length = 100)
    private String artistInstaAddr;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userEntity_id", insertable = true, updatable = true)
    private UserEntity userEntity;

    @Builder
    public void of(String profilePath, String profileName, String backImgPath, String backImgName, String artistDescription, UserEntity userEntity){
        this.profilePath = profilePath;
        this.profileName = !profileName.isBlank() ? "/"+profileName: profileName;
        this.backImgPath = backImgPath;
        this.backImgName = !backImgName.isBlank()? "/"+backImgName: backImgName;;
        this.artistDescription = artistDescription;
        this.userEntity = userEntity;
    }

    @Builder
    public void update(String profilePath, String profileName, String backImgPath, String backImgName, String artistDescription){
        this.profilePath = profilePath;
        this.profileName = !profileName.isBlank() ? "/"+profileName: profileName;
        this.backImgPath = backImgPath;
        this.backImgName = !backImgName.isBlank()? "/"+backImgName: backImgName;
        this.artistDescription = artistDescription;
    }
}
