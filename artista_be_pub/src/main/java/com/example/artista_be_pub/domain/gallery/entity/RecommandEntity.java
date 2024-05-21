package com.artista.main.domain.gallery.entity;

import com.artista.main.domain.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ART_GALLERY_RECOMMAND")
public class RecommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galleryEntity_id", insertable = true, updatable = true)
    private GalleryEntity galleryEntity;

    @Builder
    public void of(GalleryEntity galleryEntity, UserEntity userEntity){
        this.galleryEntity = galleryEntity;
    }

}
