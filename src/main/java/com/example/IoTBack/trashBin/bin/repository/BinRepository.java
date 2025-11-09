package com.example.IoTBack.trashBin.bin.repository;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinRepository extends JpaRepository<Bin, Long> {
}
