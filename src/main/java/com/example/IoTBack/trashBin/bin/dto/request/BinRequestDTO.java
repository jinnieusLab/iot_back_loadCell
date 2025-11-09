package com.example.IoTBack.trashBin.bin.dto.request;

import lombok.Getter;

public class BinRequestDTO {

    @Getter
    public static class CreateBinDTO {
        private String name;
        private String location;
    }
}
