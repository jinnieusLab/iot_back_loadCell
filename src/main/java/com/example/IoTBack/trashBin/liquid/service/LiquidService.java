package com.example.IoTBack.trashBin.liquid.service;

import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;

import java.util.List;

public interface LiquidService {

    Liquid createLiquid(Long binId, LiquidRequestDTO.CreateLiquidDTO createLiquidDTO);

    Liquid readLiquidByBinId(Long binId);

    Liquid readLiquidById(Long liquidId);

    LiquidResponseDTO.LiquidPreviewListWithAverageDTO readLiquids();

    Liquid updateLiquidByBinId(Long binId, LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO);
}
