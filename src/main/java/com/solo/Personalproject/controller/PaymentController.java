package com.solo.Personalproject.controller;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.solo.Personalproject.dto.PaymentCallbackRequest;
import com.solo.Personalproject.dto.RequestPayDto;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.entity.Order;
import com.solo.Personalproject.service.MemberService;
import com.solo.Personalproject.service.OrderService;
import com.solo.Personalproject.service.PaymentService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {


        private final PaymentService paymentService;
        private final OrderService orderService;
        private final MemberService memberService;

        // Constructor


    @GetMapping("/payment/{id}")
    public String paymentPage(@PathVariable(name = "id", required = false) String id, Model model, Principal principal) {
        System.out.println(id);

        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }

        RequestPayDto requestDto = paymentService.findRequestDto(Long.valueOf(id));
        model.addAttribute("requestDto", requestDto);

        return "order/payment";
    }

        @PostMapping(value = "/payment")
        public @ResponseBody ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
            System.out.println(request);
            IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
            return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
        }



        @GetMapping("/success-payment/{orderUid}")
        public String successPaymentPage() {
            return "order/success-payment";
        }

        @GetMapping("/fail-payment/{orderUid}")
        public String failPaymentPage(@PathVariable("orderUid") String orderUid) {
            Order order = orderService.orderUidOrderCancle(orderUid);
            return "order/fail-payment";
        }
    }