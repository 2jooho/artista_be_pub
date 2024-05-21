package com.artista.main.domain.collabor.entity;

import com.artista.main.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ART_COLLABOR_INFO")
public class CollaborEntity extends BaseEntity {
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

}
