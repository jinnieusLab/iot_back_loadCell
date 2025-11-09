package com.example.IoTBack.trashBin.bin.converter;

import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.bin.dto.request.BinRequestDTO;
import com.example.IoTBack.trashBin.bin.dto.response.BinResponseDTO;

public class BinConverter {
    public static Bin toBin(BinRequestDTO.CreateBinDTO createBinDTO) {
        return Bin.builder()
                .name(createBinDTO.getName())
                .location(createBinDTO.getLocation())
                .build();
    }

    public static BinResponseDTO.CreateBinResultDTO tocreateBinResultDTO(Bin bin) {
        return BinResponseDTO.CreateBinResultDTO.builder()
                .id(bin.getId())
                .name(bin.getName())
                .location(bin.getLocation())
                .build();
    }

    public static BinResponseDTO.BinPreviewDTO toBinPreviewDTO(Bin bin) {
        return BinResponseDTO.BinPreviewDTO.builder()
                .id(bin.getId())
                .name(bin.getName())
                .location(bin.getLocation())
                .build();
    }
}
