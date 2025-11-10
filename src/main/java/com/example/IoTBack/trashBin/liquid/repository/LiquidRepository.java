package com.example.IoTBack.trashBin.liquid.repository;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LiquidRepository extends JpaRepository<Liquid, Long> {
    Optional<Liquid> findByBin(Bin bin);

    List<Liquid> findByBinIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
            Long binId,
            LocalDateTime start,
            LocalDateTime end
    );

    // 범위 시작 이전 마지막 측정값 (이전 weight를 알기 위함)
    Optional<Liquid> findTopByBinIdAndMeasuredAtBeforeOrderByMeasuredAtDesc(
            Long binId,
            LocalDateTime before
    );
}
