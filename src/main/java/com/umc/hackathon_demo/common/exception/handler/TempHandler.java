package com.umc.hackathon_demo.common.exception.handler;

import com.umc.hackathon_demo.common.exception.GeneralException;
import com.umc.hackathon_demo.common.exception.code.BaseErrorCode;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
