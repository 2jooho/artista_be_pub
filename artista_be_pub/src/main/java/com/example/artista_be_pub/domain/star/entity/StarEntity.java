package com.artista.main.domain.star.entity;

import com.artista.main.domain.gallery.entity.GalleryEntity;
import com.artista.main.domain.user.entity.UserEntity;
import com.artista.main.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ART_STAR_INFO")
public class StarEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 작품 타이틀
     */
    @Column(nullable = false, length = 20)
    private String artTitle;

    /**
     * 작품 설명
     */
    @Column(nullable = false, length = 100)
    private String artDescription;

    /** 이미지 경로 */
    @Column(nullable = false)
    private String filePath;

    /** 이미지 명 */
    @Column(nullable = false, length = 20)
    private String fileName;

    /**
     * 선정 년도
     */
    @Column(nullable = false, length = 4)
    private Integer selectYear;

    /**
     * 선정 월
     */
    @Column(nullable = false, length = 2)
    private Integer selectMonth;

    /**
     * 우선순위
     */
    @Column(nullable = false, length = 10)
    private Integer orderNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galleryEntity_id", insertable = true, updatable = true)
    private GalleryEntity galleryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userEntity_id", insertable = true, updatable = true)
    private UserEntity userEntity;

    @Builder
    public void of(String artTitle, String artDescription, String filePath, String fileName, Integer selectYear, Integer selectMonth, Integer orderNo, GalleryEntity gallery){
        this.artTitle = artTitle;
        this.artDescription = artDescription;
        this.filePath = filePath;
        this.fileName = fileName;
        this.selectYear = selectYear;
        this.selectMonth = selectMonth;
        this.orderNo = orderNo;
        this.galleryEntity = gallery;
    }
}
