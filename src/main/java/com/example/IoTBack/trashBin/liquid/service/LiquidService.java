package com.example.IoTBack.trashBin.liquid.service;

import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;

import java.util.List;

public interface LiquidService {

    Liquid createLiquid(Long binId, LiquidRequestDTO.CreateLiquidDTO createLiquidDTO);

    Liquid readLiquidByBinId(Long binId);

    Liquid readLiquidById(Long liquidId);

    List<Liquid> readLiquids();
}
