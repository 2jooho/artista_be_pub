package com.artista.main.domain.gallery.entity;

import com.artista.main.domain.user.entity.UserEntity;
import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ART_GALLERY_LIKE")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galleryEntity_id", insertable = true, updatable = true)
    private GalleryEntity galleryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userEntity_id", insertable = true, updatable = true)
    private UserEntity userEntity;

    @Builder
    public void of(GalleryEntity galleryEntity, UserEntity userEntity){
        this.galleryEntity = galleryEntity;
        this.userEntity = userEntity;
    }
}
