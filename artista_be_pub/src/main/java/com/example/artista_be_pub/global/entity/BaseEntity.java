package com.artista.main.global.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    /** 등록일시 */
    @CreatedDate
    @Column
    private LocalDateTime rgsttDtm;

    /** 등록자 */
    @Column(length = 20)
    private String rgpsId = "API";

    /** 수정일시 */
    @Column
    protected LocalDateTime editDtm;

    /** 수정자 */
    @Column(length = 20)
    protected String upusId;
}