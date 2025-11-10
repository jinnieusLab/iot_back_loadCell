package com.example.IoTBack.trashBin.liquid.repository;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LiquidRepository extends JpaRepository<Liquid, Long> {
    Optional<Liquid> findByBin(Bin bin);
}
