package com.solo.Personalproject.controller;

import com.solo.Personalproject.dto.CartDetailDto;
import com.solo.Personalproject.dto.CartItemDto;
import com.solo.Personalproject.dto.CartOrderDto;
import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.service.CartService;
import com.solo.Personalproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final MemberService memberService;

    @PostMapping(value = "/cart")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto,
                         BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        Long cartItemId;
        String jsonResponse ="";
        try {
            cartItemId = cartService.addCart(cartItemDto,email);
            jsonResponse = "{\"message\": \"" + cartItemId + "\"}";
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems",cartDetailDtoList);
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자의 이메일 가져오기
            Member member = memberService.memberload(email); // 이메일로 Member 객체 조회

            if (member != null && member.getName() != null) {
                model.addAttribute("name", member.getName()); // 실제 사용자 이름 추가
            } else {
                model.addAttribute("name", "회원"); // 사용자 이름을 못 찾았을 때 대체 텍스트
            }
        }
        return "cart/cartList";
    }
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       int count, Principal principal){
        System.out.println(cartItemId);
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개이상 담아주세요.",HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<String>(String.valueOf(cartItemId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       Principal principal){
        if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        String jsonResponse = "{\"message\": \"" + cartItemId + "\"}";
        return  new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto,
                                                      Principal principal){

        System.out.println(cartOrderDto.getCartItemId());
        //CartOrderDtoList List <- getCartOrderList 통해서 views에서 내려온 List
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        // null or size == 0 이면 실행
        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문하실 상품을 선택해주세요.",HttpStatus.FORBIDDEN);
        }

        // 전체 유효성 검사
        for (CartOrderDto cartOrder : cartOrderDtoList){
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }


        Long orderId;
        String jsonResponse ="";
        try {

            // cart -> order
            // cartService -> orderService
            // cartOrderDtoList(CartOrderDtoList)
            orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());

            jsonResponse = "{\"message\": \"" + orderId + "\"}";

        }
        catch (Exception e){ // 실패
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        // 성공
        return  new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
}
