package com.umc.hackathon_demo.domain.test.service;

import com.umc.hackathon_demo.common.exception.code.status.ErrorStatus;
import com.umc.hackathon_demo.common.exception.handler.TempHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestQueryServiceImpl implements TestQueryService {
    @Override
    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }
}