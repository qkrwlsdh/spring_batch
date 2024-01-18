package com.bankedin.batch.repository;

import com.bankedin.batch.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository  extends JpaRepository<BoardEntity, Long> {
    // 특정 날짜 범위 내의 일별 board count 조회
    @Query("SELECT DATE_FORMAT(b.createdDt, '%Y-%m-%d') AS day, COUNT(b) FROM BoardEntity b WHERE b.createdDt BETWEEN :startDate AND :endDate GROUP BY DAY(b.createdDt)")
    List<Object[]> countDailyBoardByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
