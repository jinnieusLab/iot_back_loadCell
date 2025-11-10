package com.example.IoTBack.trashBin.liquid.service.impl;


import com.example.IoTBack.global.PeriodType;
import com.example.IoTBack.global.TrendMode;
import com.example.IoTBack.global.apiPayload.code.status.ErrorStatus;
import com.example.IoTBack.global.apiPayload.exception.handler.BinHandler;
import com.example.IoTBack.global.apiPayload.exception.handler.LiquidHandler;
import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.bin.repository.BinRepository;
import com.example.IoTBack.trashBin.liquid.converter.LiquidConverter;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.domain.LiquidHistory;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;
import com.example.IoTBack.trashBin.liquid.repository.LiquidHistoryRepository;
import com.example.IoTBack.trashBin.liquid.repository.LiquidRepository;
import com.example.IoTBack.trashBin.liquid.service.LiquidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LiquidServiceImpl implements LiquidService {
    private final LiquidRepository liquidRepository;
    private final BinRepository binRepository;
    private final LiquidHistoryRepository liquidHistoryRepository;

    @Override
    public Liquid createLiquid(Long binId, LiquidRequestDTO.CreateLiquidDTO createLiquidDTO) {
        Liquid liquid = LiquidConverter.toLiquid(createLiquidDTO);
        Bin bin = binRepository.findById(binId).orElseThrow(() -> {
            throw new BinHandler(ErrorStatus._NOT_FOUND_BIN);
        });
        liquid.setBin(bin);
        return liquidRepository.save(liquid);
    }

    @Transactional(readOnly = true)
    @Override
    public Liquid readLiquidByBinId(Long binId) {
        Bin bin = binRepository.findById(binId).orElseThrow(() -> {
            throw new BinHandler(ErrorStatus._NOT_FOUND_BIN);
        });

        return liquidRepository.findByBin(bin).orElseThrow(() -> {
            throw new LiquidHandler(ErrorStatus._NOT_FOUND_LIQUID);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Liquid readLiquidById(Long liquidId) {
        return liquidRepository.findById(liquidId).orElseThrow(() -> {
            throw new LiquidHandler(ErrorStatus._NOT_FOUND_LIQUID);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public LiquidResponseDTO.LiquidPreviewListWithAverageDTO readLiquids() {
        List<Liquid> liquids = liquidRepository.findAll();

        List<LiquidResponseDTO.LiquidPreviewDTO> items = liquids.stream()
                .map(LiquidConverter::toLiquidPreviewDTO)
                .toList();

        // 전체 물통 평균 무게 계산
        double averageWeight = items.isEmpty() ? 0.0 :
                items.stream()
                        .mapToDouble(LiquidResponseDTO.LiquidPreviewDTO::getWeight)
                        .average()
                        .orElse(0.0);

        return LiquidConverter.toLiquidPreviewListWithAverageDTO(liquids, averageWeight);
    }

    @Override
    public Liquid updateLiquidByBinId(Long binId, LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO) {
        Bin bin = binRepository.findById(binId).orElseThrow(() -> {
            throw new BinHandler(ErrorStatus._NOT_FOUND_BIN);
        });

        Liquid liquid = liquidRepository.findByBin(bin).orElseThrow(() -> {
            throw new LiquidHandler(ErrorStatus._NOT_FOUND_LIQUID);
        });

        // weight, addedWeight 업데이트
        updateLiquidWeight(liquid, updateLiquidDTO.getWeight());

        // LiquidHistory 기록 추가
        addLiquidHistory(liquid);

        return liquid;
    }

    @Override
    public Liquid updateLiquidById(Long liquidId, LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO) {
        Liquid liquid = liquidRepository.findById(liquidId).orElseThrow(() -> {
            throw new LiquidHandler(ErrorStatus._NOT_FOUND_LIQUID);
        });

        // weight, addedWeight 업데이트
        updateLiquidWeight(liquid, updateLiquidDTO.getWeight());

        // LiquidHistory 기록 추가
        addLiquidHistory(liquid);

        return liquid;
    }

    @Override
    public Object readLiquidTrendByBinId(Long binId, PeriodType period, LocalDate date, TrendMode mode) {
        binRepository.findById(binId).orElseThrow(() -> {
            throw new BinHandler(ErrorStatus._NOT_FOUND_BIN);
        });

        if (date == null) {
            date = LocalDate.now();
        }

        // 1) period에 따라 조회 기간(start, end) 계산
        LocalDateTime start;
        LocalDateTime end;

        switch (period) {
            case MONTHLY -> {
                LocalDate firstDay = date.withDayOfMonth(1);
                start = firstDay.atStartOfDay();
                end = firstDay.plusMonths(1).atStartOfDay();
            }
            case WEEKLY -> {
                WeekFields wf = WeekFields.ISO;
                LocalDate firstDayOfWeek = date.with(wf.dayOfWeek(), 1); // 월요일 기준
                start = firstDayOfWeek.atStartOfDay();
                end = firstDayOfWeek.plusWeeks(1).atStartOfDay();
            }
            case DAILY -> {
                start = date.atStartOfDay();
                end = start.plusDays(1);
            }
            default -> throw new IllegalArgumentException("Unsupported period: " + period);
        }

        // 2) 기간 내 LiquidHistory 조회
        // 필드가 `Bin bin;` 라면 메서드는 보통 findByBin_Id... 로 가는 게 안전함
        List<LiquidHistory> histories = liquidHistoryRepository
                .findByBinIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(binId, start, end);

        // 3) Converter에서 mode에 따라 변환
        return LiquidConverter.toLiquidTrendDTO(binId, histories, period, mode);
    }

    @Override
    public LiquidResponseDTO.LiquidTotalTrendDTO readLiquidTotalTrendById(Long liquidId, PeriodType period, LocalDate date) {
        Liquid liquid = liquidRepository.findById(liquidId).orElseThrow(() -> {
            throw new LiquidHandler(ErrorStatus._NOT_FOUND_LIQUID);
        });

        Long binId = liquid.getBin().getId();

//        return computeLiquidTrend(binId, period, date);
        return null;
    }

    public void updateLiquidWeight(Liquid liquid, double newWeight) {
        // addedWeight 업데이트
        double oldWeight = liquid.getWeight();
        double addedWeight = (newWeight > oldWeight) ? newWeight-oldWeight: 0;

        // weight 업데이트
        liquid.update(newWeight, addedWeight, LocalDateTime.now());
    }

    public void addLiquidHistory(Liquid liquid) {
        LiquidHistory history = LiquidHistory.builder()
                .bin(liquid.getBin())
                .liquid(liquid)
                .weight(liquid.getWeight())
                .addedWeight(liquid.getAddedWeight())
                .measuredAt(LocalDateTime.now())
                .build();

        liquidHistoryRepository.save(history);
    }
}

