package com.solo.Personalproject.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;

import com.solo.Personalproject.dto.PaymentCallbackRequest;
import com.solo.Personalproject.dto.RequestPayDto;
import com.solo.Personalproject.entity.Order;
import com.solo.Personalproject.repository.OrderRepository;
import com.solo.Personalproject.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

import static com.solo.Personalproject.constant.PaymentStatus.OK;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    @Override
    public RequestPayDto findRequestDto(Long id) {

        Order order = orderRepository.findOrderAndPaymentAndMember(id)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        return RequestPayDto.builder()
                .buyerName(order.getMember().getName())
                .buyerEmail(order.getMember().getEmail())
                .buyerAddress(order.getMember().getAddress())
                .paymentPrice(order.getPayment().getPrice())
                .orderUid(order.getPayment().getPaymentUid())
                .itemName(order.getOrderItems().getFirst().getItem().getItemNm())
                .build();
    }


    @Override
    public IamportResponse<com.siot.IamportRestClient.response.Payment> paymentByCallback(PaymentCallbackRequest request) {

        try {

            System.out.println(request);
            // 결제 단건 조회(아임포트)
            IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());


            System.out.println(request);
            // 주문내역 조회
            Order order = orderRepository.findOrderAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            int price = order.getPayment().getPrice();
            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if(iamportPrice != price) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }

            // 결제 상태 변경
            order.getPayment().changePaymentBySuccess(OK, iamportResponse.getResponse().getImpUid());

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public com.solo.Personalproject.entity.Payment paymentSearch(String Uid){
        com.solo.Personalproject.entity.Payment payment =paymentRepository.findByPaymentUid(Uid);
        return payment;

    }
}
