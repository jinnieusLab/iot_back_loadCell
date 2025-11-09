package com.example.IoTBack.trashBin.liquid.controller;

import com.example.IoTBack.global.apiPayload.BaseResponse;
import com.example.IoTBack.trashBin.liquid.converter.LiquidConverter;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;
import com.example.IoTBack.trashBin.liquid.service.LiquidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors/weight/liquids")
public class LiquidController {
    private final LiquidService liquidService;

    @PostMapping("/by-bin/{binId}")
    public BaseResponse<LiquidResponseDTO.CreateLiquidResultDTO> createLiquid(@PathVariable Long binId, @RequestBody LiquidRequestDTO.CreateLiquidDTO createLiquidDTO) {
        Liquid liquid = liquidService.createLiquid(binId, createLiquidDTO);
        return BaseResponse.onSuccess(LiquidConverter.toCreateLiquidResultDTO(liquid));
    }

    // binId로 liquid 조회
    @GetMapping("/by-bin/{binId}")
    public BaseResponse<LiquidResponseDTO.LiquidPreviewDTO> readLiquidByBinId(@PathVariable Long binId) {
        Liquid liquid = liquidService.readLiquidByBinId(binId);
        return BaseResponse.onSuccess(LiquidConverter.toLiquidPreviewDTO(liquid));
    }

    // liquidId로 liquid 조회
    @GetMapping("/{liquidId}")
    public BaseResponse<LiquidResponseDTO.LiquidPreviewDTO> readLiquiBbyId(@PathVariable Long liquidId) {
        Liquid liquid = liquidService.readLiquidById(liquidId);
        return BaseResponse.onSuccess(LiquidConverter.toLiquidPreviewDTO(liquid));
    }
}
