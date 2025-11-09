package com.example.IoTBack.global.apiPayload.exception.handler;

import com.example.IoTBack.global.apiPayload.code.BaseErrorCode;
import com.example.IoTBack.global.apiPayload.exception.GeneralException;

public class LiquidHandler extends GeneralException {
    public LiquidHandler(BaseErrorCode code) {
        super(code);
    }
}
