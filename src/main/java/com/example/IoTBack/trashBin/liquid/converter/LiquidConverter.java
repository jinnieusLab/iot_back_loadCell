package com.example.IoTBack.trashBin.liquid.converter;


import com.example.IoTBack.global.PeriodType;
import com.example.IoTBack.global.TrendMode;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.domain.LiquidHistory;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;

import java.util.ArrayList;
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

    public static LiquidResponseDTO.LiquidTrendDTO toLiquidTrendDTO(List<LiquidHistory> histories, PeriodType period, TrendMode mode) {
        List<LiquidResponseDTO.LiquidHistoryPreviewDTO> points = new ArrayList<>();

        if (mode == TrendMode.TREND) {
            // 단순 트렌드: 각각의 측정 weight 그대로 사용
            points = histories.stream()
                    .map(LiquidConverter::toLiquidHistoryPreviewDTO)
                    .toList();
        }

        else {
            // 누적합: addedWeight를 누적해서 weight에 넣어줌
            double accumulatedTotal = 0.0;

            for (LiquidHistory h : histories) {
                double addedWeight = h.getAddedWeight() != 0 ? h.getAddedWeight() : 0.0;
                accumulatedTotal += addedWeight;

                points.add(
                        LiquidResponseDTO.LiquidHistoryPreviewDTO.builder()
                                .id(h.getId())
                                .weight(accumulatedTotal)              // 누적합
                                .measuredAt(h.getMeasuredAt())
                                .overload(h.getOverload())
                                .binId(h.getBin().getId())
                                .build()
                );
            }
        }

        return LiquidResponseDTO.LiquidTrendDTO.builder()
                .liquidHistoryPreviewDTOs(points)
                .period(period)
                .build();
    }
}
