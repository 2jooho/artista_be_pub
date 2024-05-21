package com.artista.main.domain.category.entity;

import com.artista.main.domain.gallery.entity.GalleryEntity;
import com.artista.main.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art_category")
public class CategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 카테고리 명
     */
    @Column(nullable = false, length = 100)
    private String categoryName;

    /**
     * 카테고리 순번
     */
    @Column(nullable = false, length = 3)
    private Integer orderNo;

    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GalleryEntity> galleryEntities = new ArrayList<>();

}
