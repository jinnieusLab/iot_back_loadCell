package com.example.IoTBack.trashBin.liquid.repository;

import com.example.IoTBack.trashBin.liquid.domain.LiquidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquidHistoryRepository extends JpaRepository<LiquidHistory, Long> {
}
