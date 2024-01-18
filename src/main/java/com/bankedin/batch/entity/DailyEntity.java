package com.bankedin.batch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOARD_DAILY_STATS")
public class DailyEntity {

	@Id
	@Column(name = "BOARD_CNT")
	private int boardCnt;

	@Column(name = "DAY")
	private String day;

	public void statsBoard(BoardEntity boardEntity) {
		this.day = boardEntity.getDay();
		this.boardCnt = boardEntity.getBoardCnt();
	}

}
