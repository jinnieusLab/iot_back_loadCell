package com.example.IoTBack.trashBin.liquid.converter;


import com.example.IoTBack.global.PeriodType;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.domain.LiquidHistory;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;

import java.util.List;

public class LiquidConverter {
    public static Liquid toLiquid(LiquidRequestDTO.CreateLiquidDTO createLiquidDTO) {
        return Liquid.builder()
                .weight(createLiquidDTO.getWeight())
                .build();
    }

    public static LiquidResponseDTO.CreateLiquidResultDTO toCreateLiquidResultDTO(Liquid liquid) {
        return LiquidResponseDTO.CreateLiquidResultDTO.builder()
                .id(liquid.getId())
                .binId(liquid.getBin().getId())
                .build();
    }

    public static LiquidResponseDTO.LiquidPreviewDTO toLiquidPreviewDTO(Liquid liquid) {
        return LiquidResponseDTO.LiquidPreviewDTO.builder()
                .id(liquid.getId())
                .weight(liquid.getWeight())
                .measuredAt(liquid.getMeasuredAt())
                .overload(liquid.getOverload())
                .binId(liquid.getBin().getId())
                .build();
    }

    public static LiquidResponseDTO.LiquidPreviewListWithAverageDTO toLiquidPreviewListWithAverageDTO(List<Liquid> liquids, double averageWeight) {
        List<LiquidResponseDTO.LiquidPreviewDTO> liquidPreviewDTOs = liquids.stream()
                .map(LiquidConverter::toLiquidPreviewDTO).toList();

        return LiquidResponseDTO.LiquidPreviewListWithAverageDTO.builder()
                .liquidPreviewDTOs(liquidPreviewDTOs)
                .averageWeight(averageWeight)
                .build();
    }

    public static LiquidResponseDTO.LiquidHistoryPreviewDTO toLiquidHistoryPreviewDTO(LiquidHistory liquid) {
        return LiquidResponseDTO.LiquidHistoryPreviewDTO.builder()
                .id(liquid.getId())
                .weight(liquid.getWeight())
                .measuredAt(liquid.getMeasuredAt())
                .overload(liquid.getOverload())
                .binId(liquid.getBin().getId())
                .build();
    }

    public static LiquidResponseDTO.LiquidTrendDTO toLiquidTrendDTO(List<LiquidHistory> liquids, PeriodType period) {
        List<LiquidResponseDTO.LiquidHistoryPreviewDTO> liquidHistoryPreviewDTOs = liquids.stream()
                .map(LiquidConverter::toLiquidHistoryPreviewDTO).toList();

        return LiquidResponseDTO.LiquidTrendDTO.builder()
                .liquidHistoryPreviewDTOs(liquidHistoryPreviewDTOs)
                .period(period)
                .build();
    }
}
