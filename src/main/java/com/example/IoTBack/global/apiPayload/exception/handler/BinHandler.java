package com.example.IoTBack.global.apiPayload.exception.handler;

import com.example.IoTBack.global.apiPayload.code.BaseErrorCode;
import com.example.IoTBack.global.apiPayload.exception.GeneralException;

public class BinHandler extends GeneralException {
    public BinHandler(BaseErrorCode code) {
        super(code);
    }
}
