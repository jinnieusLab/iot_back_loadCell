package com.example.IoTBack.trashBin.bin.service.impl;

import com.example.IoTBack.global.apiPayload.code.status.ErrorStatus;
import com.example.IoTBack.global.apiPayload.exception.handler.BinHandler;
import com.example.IoTBack.trashBin.bin.converter.BinConverter;
import com.example.IoTBack.trashBin.bin.domain.Bin;
import com.example.IoTBack.trashBin.bin.dto.request.BinRequestDTO;
import com.example.IoTBack.trashBin.bin.repository.BinRepository;
import com.example.IoTBack.trashBin.bin.service.BinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
@Transactional
public class BinServiceImpl implements BinService {
    private final BinRepository binRepository;

    @Override
    public Bin createBin(BinRequestDTO.CreateBinDTO createBinDTO) {
        Bin bin = BinConverter.toBin(createBinDTO);
        return binRepository.save(bin);
    }

    @Transactional(readOnly = true)
    @Override
    public Bin readBin(@PathVariable Long binId) {
        return binRepository.findById(binId).orElseThrow(() -> {
            throw new BinHandler(ErrorStatus._NOT_FOUND_BIN);
        });
    }
}
