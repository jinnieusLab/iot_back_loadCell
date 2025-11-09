package com.example.IoTBack.trashBin.liquid.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class LiquidResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateLiquidResultDTO {
        private Long id;
        private Long binId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LiquidPreviewDTO {
        private Long id;
        private double weight;
        private LocalDateTime measuredAt;
        private Boolean overload;
        private Long binId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LiquidPreviewListWithAverageDTO {
        List<LiquidPreviewDTO> liquidPreviewDTOs;
        private double averageWeight;
    }
}
