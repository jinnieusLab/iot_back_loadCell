package com.example.IoTBack.trashBin.liquid.repository;

import com.example.IoTBack.trashBin.liquid.domain.LiquidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LiquidHistoryRepository extends JpaRepository<LiquidHistory, Long> {
    List<LiquidHistory> findByBinIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(Long binId, LocalDateTime start, LocalDateTime end);
}
