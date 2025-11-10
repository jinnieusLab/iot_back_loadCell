package com.example.IoTBack.trashBin.liquid.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

public class LiquidRequestDTO {

    @Getter
    public static class CreateLiquidDTO {
        private double weight;
    }

    @Getter
    public static class UpdateLiquidDTO {
        private double weight;
    }
}
