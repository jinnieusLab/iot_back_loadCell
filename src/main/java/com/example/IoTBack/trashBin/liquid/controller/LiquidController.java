package com.example.IoTBack.trashBin.liquid.controller;

import com.example.IoTBack.global.apiPayload.BaseResponse;
import com.example.IoTBack.trashBin.liquid.converter.LiquidConverter;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;
import com.example.IoTBack.trashBin.liquid.service.LiquidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors/weight/liquids")
public class LiquidController {
    private final LiquidService liquidService;

    // 물통 생성
    // 쓰레기통 생성 시 반드시 물통 생성
    @PostMapping("/by-bin/{binId}")
    public BaseResponse<LiquidResponseDTO.CreateLiquidResultDTO> createLiquid(@PathVariable Long binId, @RequestBody LiquidRequestDTO.CreateLiquidDTO createLiquidDTO) {
        Liquid liquid = liquidService.createLiquid(binId, createLiquidDTO);
        return BaseResponse.onSuccess(LiquidConverter.toCreateLiquidResultDTO(liquid));
    }

    // binId로 liquid 정보 조회
    @GetMapping("/by-bin/{binId}")
    public BaseResponse<LiquidResponseDTO.LiquidPreviewDTO> readLiquidByBinId(@PathVariable Long binId) {
        Liquid liquid = liquidService.readLiquidByBinId(binId);
        return BaseResponse.onSuccess(LiquidConverter.toLiquidPreviewDTO(liquid));
    }

    // liquidId로 liquid 정보 조회
    @GetMapping("/{liquidId}")
    public BaseResponse<LiquidResponseDTO.LiquidPreviewDTO> readLiquidBbyId(@PathVariable Long liquidId) {
        Liquid liquid = liquidService.readLiquidById(liquidId);
        return BaseResponse.onSuccess(LiquidConverter.toLiquidPreviewDTO(liquid));
    }

    // 전체 liquid 정보 + 전체 무게 평균 조회
    @GetMapping
    public BaseResponse<LiquidResponseDTO.LiquidPreviewListWithAverageDTO> readLiquids() {
        LiquidResponseDTO.LiquidPreviewListWithAverageDTO liquidsWithAverage = liquidService.readLiquids();
        return BaseResponse.onSuccess(liquidsWithAverage);
    }

    // 특정 물통 무게 수정 (by binId)
    @PatchMapping("/by-bin/{binId}")
    public BaseResponse<LiquidResponseDTO.LiquidPreviewDTO> updateLiquidByBinId(@PathVariable Long binId, @RequestBody LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO) {
        Liquid liquid = liquidService.updateLiquidByBinId(binId, updateLiquidDTO);
        return BaseResponse.onSuccess(LiquidConverter.toLiquidPreviewDTO(liquid));
    }

    // 특정 물통 무게 수정 (by liquidId)
    @PatchMapping("/{liquidId}")
    public BaseResponse<LiquidResponseDTO.LiquidPreviewDTO> updateLiquidById(@PathVariable Long liquidId, @RequestBody LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO) {
        Liquid liquid = liquidService.updateLiquidById(liquidId, updateLiquidDTO);
        return BaseResponse.onSuccess(LiquidConverter.toLiquidPreviewDTO(liquid));
    }

    // 특정 물통 무게 트랜드 조회
//    @GetMapping("/")
}
