package com.artista.main.domain.gallery.entity;

import com.artista.main.domain.category.entity.CategoryEntity;
import com.artista.main.domain.star.entity.StarEntity;
import com.artista.main.domain.user.entity.UserEntity;
import com.artista.main.global.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ART_GALLERY_INFO")
@DynamicUpdate
public class GalleryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 작품 타이틀
     */
    @Column(nullable = false)
    private String artTitle;

    /**
     * 작품 설명
     */
    @Column(nullable = false)
    private String artDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userEntity_id", insertable = true, updatable = true)
    private UserEntity userEntity;

    @OneToOne(mappedBy = "galleryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private StarEntity starEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryEntity_id")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "galleryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ArtImgEntity> artImgEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "galleryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RecommandEntity recommandEntity;

    @Builder
    public void galleryUpload(String artTitle, String artDescription, UserEntity user, StarEntity star, CategoryEntity category, List<ArtImgEntity> artImgEntityList){
        this.artTitle = artTitle;
        this.artDescription = artDescription;
        this.userEntity = user;
        this.starEntity = star;
        this.categoryEntity = category;
        this.artImgEntityList = artImgEntityList;
    }

    @Builder
    public void update(String artTitle, String artDescription, CategoryEntity categoryEntity){
        this.artTitle = artTitle;
        this.artDescription = artDescription;
        this.categoryEntity = categoryEntity;
    }

}
