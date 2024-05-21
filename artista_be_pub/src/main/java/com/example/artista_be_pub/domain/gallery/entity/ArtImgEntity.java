package com.artista.main.domain.gallery.entity;

import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "art_img")
public class ArtImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 이미지 경로 */
    private String filePath;

    /** 이미지 명 */
    private String fileName;

    /** 이미지 순위 */
    private Integer orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galleryEntity_id", insertable = true, updatable = true)
    private GalleryEntity galleryEntity;

    @Builder
    public void of(String filePath, String fileName, Integer orderNo, GalleryEntity galleryEntity){
        this.filePath = filePath;
        this.fileName = fileName;
        this.orderNo = orderNo;
        this.galleryEntity = galleryEntity;
    }

    @Builder
    public void update(String filePath, String fileName, Integer orderNo){
        this.filePath = filePath;
        this.fileName = fileName;
    }
}
