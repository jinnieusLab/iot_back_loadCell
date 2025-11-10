package com.example.IoTBack.trashBin.liquid.service.impl;


import com.example.IoTBack.global.PeriodType;
import com.example.IoTBack.global.apiPayload.code.status.ErrorStatus;
import com.example.IoTBack.global.apiPayload.exception.handler.BinHandler;
import com.example.IoTBack.global.apiPayload.exception.handler.LiquidHandler;
import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.bin.repository.BinRepository;
import com.example.IoTBack.trashBin.liquid.converter.LiquidConverter;
import com.example.IoTBack.trashBin.liquid.domain.Liquid;
import com.example.IoTBack.trashBin.liquid.dto.request.LiquidRequestDTO;
import com.example.IoTBack.trashBin.liquid.dto.response.LiquidResponseDTO;
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

        // addedWeight 업데이트
        double oldWeight = liquid.getWeight();
        double newWeight = updateLiquidDTO.getWeight();
        double addedWeight = (newWeight > oldWeight) ? newWeight-oldWeight: 0;

        // weight 업데이트
        liquid.update(updateLiquidDTO.getWeight(), addedWeight, LocalDateTime.now());
        return liquid;
    }

    @Override
    public Liquid updateLiquidById(Long liquidId, LiquidRequestDTO.UpdateLiquidDTO updateLiquidDTO) {
        Liquid liquid = liquidRepository.findById(liquidId).orElseThrow(() -> {
            throw new LiquidHandler(ErrorStatus._NOT_FOUND_LIQUID);
        });

        // addedWeight 업데이트
        double oldWeight = liquid.getWeight();
        double newWeight = updateLiquidDTO.getWeight();
        double addedWeight = (newWeight > oldWeight) ? newWeight-oldWeight: 0;

        // weight 업데이트
        liquid.update(updateLiquidDTO.getWeight(), addedWeight, LocalDateTime.now());
        return liquid;
    }

    @Override
    public LiquidResponseDTO.LiquidTotalTrendDTO readLiquidTotalTrend(Long binId, PeriodType period, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }

        // period에 따라 조회 기간(start, end) 계산
        LocalDateTime start;
        LocalDateTime end;

        switch (period) {
            // 월별 조회
            case MONTHLY -> {
                LocalDate firstDay = date.withDayOfMonth(1);
                start = firstDay.atStartOfDay();
                end = firstDay.plusMonths(1).atStartOfDay();
            }
            // 주별 조회
            case WEEKLY -> {
                WeekFields wf = WeekFields.ISO;
                LocalDate firstDayOfWeek = date.with(wf.dayOfWeek(), 1); // 월요일 기준
                start = firstDayOfWeek.atStartOfDay();
                end = firstDayOfWeek.plusWeeks(1).atStartOfDay();
            }
            // 하루 조회
            case DAILY -> {
                start = date.atStartOfDay();
                end = start.plusDays(1);
            }
            default -> throw new IllegalArgumentException("Unsupported period: " + period);
        }

        // DB에서 해당 기간의 Liquid 데이터 조회
        List<Liquid> liquids = liquidRepository
                .findByBinIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(binId, start, end);
        Map<String, Double> bucketTotals = new LinkedHashMap<>();

        // addedWeight 합산
        for (Liquid liquid : liquids) {
            LocalDateTime measuredAt = liquid.getMeasuredAt();
            if (measuredAt == null) continue;

            String key;
            switch (period) {
                case MONTHLY -> {
                    // 날짜별 (예: 2025-11-10)
                    key = measuredAt.toLocalDate().toString();
                }
                case WEEKLY -> {
                    // 날짜별 (주 내 날짜들)
                    key = measuredAt.toLocalDate().toString();
                }
                case DAILY -> {
                    // 시간별 (예: 13:00)
                    int hour = measuredAt.getHour();
                    key = String.format("%02d:00", hour);
                }
                default -> throw new IllegalStateException("Unexpected value: " + period);
            }

            double added = liquid.getAddedWeight() != 0 ? liquid.getAddedWeight() : 0.0;
            bucketTotals.merge(key, added, Double::sum);
        }

        // 구간별 포인트 리스트 생성
        List<LiquidResponseDTO.LiquidTotalTrendPointDTO> points = bucketTotals.entrySet().stream()
                .map(e -> LiquidResponseDTO.LiquidTotalTrendPointDTO.builder()
                        .label(e.getKey())
                        .totalWeight(e.getValue())
                        .build())
                .toList();

        // 전체 누적 합 계산
        double totalWeight = bucketTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        return LiquidResponseDTO.LiquidTotalTrendDTO.builder()
                .binId(binId)
                .period(period)
                .points(points)
                .totalWeight(totalWeight)
                .build();
    }
}

