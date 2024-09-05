package com.solo.Personalproject.service;

import com.siot.IamportRestClient.response.IamportResponse;

import com.siot.IamportRestClient.response.Payment;
import com.solo.Personalproject.dto.PaymentCallbackRequest;
import com.solo.Personalproject.dto.RequestPayDto;

public interface PaymentService {
    RequestPayDto findRequestDto(Long id);
    // 결제(콜백)
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);

    com.solo.Personalproject.entity.Payment paymentSearch(String UId);
}
