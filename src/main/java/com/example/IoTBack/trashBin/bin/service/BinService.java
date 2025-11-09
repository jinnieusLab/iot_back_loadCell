package com.example.IoTBack.trashBin.bin.service;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.bin.dto.request.BinRequestDTO;

public interface BinService {
    Bin createBin(BinRequestDTO.CreateBinDTO createBinDTO);

    Bin readBin(Long id);
}
