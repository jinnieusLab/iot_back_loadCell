package com.example.IoTBack.trashBin.liquid.service;

import com.example.IoTBack.global.PeriodType;
import com.example.IoTBack.global.TrendMode;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;

import java.time.LocalDate;

public interface LiquidService {

    Liquid createLiquid(Long binId, LiquidRequestDTO.CreateLiquidDTO createLiquidDTO);

    Liquid readLiquidByBinId(Long binId);

    Liquid readLiquidById(Long liquidId);

    LiquidResponseDTO.LiquidPreviewListWithAverageDTO readLiquids();

    Liquid updateLiquidByBinId(Long binId, LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO);

    Liquid updateLiquidById(Long liquidId, LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO);

    Object readLiquidTrendByBinId(Long binId, PeriodType period, LocalDate date, TrendMode mode);

    Object readLiquidTrendById(Long liquidId, PeriodType period, LocalDate date, TrendMode mode);
}
