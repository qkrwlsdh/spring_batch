package com.bankedin.batch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOARD")
public class BoardEntity {
	
	@Id 
    @Column(name = "ID")
	private Long id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CREATED_DT")
    private LocalDateTime createdDt;

    /**
     * Transient 란,
     * JPA 어노테이션으로, 이 필드가 임시로 사용되거나 데이터베이스에 저장되지 않을 것을 나타냄
     */
    @Transient
    private String day;

    @Transient
    private int boardCnt;
}