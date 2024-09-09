package com.solo.Personalproject.controller;

import com.solo.Personalproject.dto.OrderDto;
import com.solo.Personalproject.dto.OrderHistDto;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.service.MemberService;
import com.solo.Personalproject.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity<String> order(
            @RequestBody @Valid OrderDto orderDto,
            BindingResult bindingResult,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;
        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 사용자 이름 가져오기
        String userName = "회원"; // 기본값
        if (email != null) {
            Member member = memberService.memberload(email);
            if (member != null && member.getName() != null) {
                userName = member.getName();
            }
        }

        // JSON 응답에 사용자 이름 포함
        String jsonResponse = String.format("{\"orderId\": \"%d\", \"userName\": \"%s\"}", orderId, userName);
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        // 사용자 이름 가져오기
        String userName = "회원"; // 기본값
        String email = principal.getName();
        if (email != null) {
            Member member = memberService.memberload(email);
            if (member != null && member.getName() != null) {
                userName = member.getName();
            }
        }

        model.addAttribute("name", userName);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){
        if (!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        String jsonResponse = "{\"orderId\": \"" + orderId + "\"}";
        return  new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
}
