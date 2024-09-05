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

            Member member = memberService.memberload(principal.getName());
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

