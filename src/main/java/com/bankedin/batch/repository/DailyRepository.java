package com.bankedin.batch.repository;

import com.bankedin.batch.entity.DailyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<DailyEntity, String>{

}
