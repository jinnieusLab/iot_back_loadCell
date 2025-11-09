package com.example.IoTBack.trashBin.bin.controller;

import com.example.IoTBack.global.apiPayload.BaseResponse;
import com.example.IoTBack.trashBin.bin.converter.BinConverter;
import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.bin.dto.request.BinRequestDTO;
import com.example.IoTBack.trashBin.bin.dto.response.BinResponseDTO;
import com.example.IoTBack.trashBin.bin.service.BinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bin")
public class BinController {
    private final BinService binService;

    @PostMapping
    public BaseResponse<BinResponseDTO.CreateBinResultDTO> createBin(@RequestBody BinRequestDTO.CreateBinDTO createBinDTO) {
        Bin bin = binService.createBin(createBinDTO);
        return BaseResponse.onSuccess(BinConverter.tocreateBinResultDTO(bin));
    }

    @GetMapping("/{binId}")
    public BaseResponse<BinResponseDTO.BinPreviewDTO> readBin(Long binId) {
        Bin bin = binService.readBin(binId);
        return BaseResponse.onSuccess(BinConverter.toBinPreviewDTO(bin));
    }
}