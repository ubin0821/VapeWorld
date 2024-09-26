package com.solo.Personalproject.service;

import com.solo.Personalproject.constant.PaymentStatus;
import com.solo.Personalproject.dto.OrderDto;
import com.solo.Personalproject.dto.OrderHistDto;
import com.solo.Personalproject.dto.OrderItemDto;
import com.solo.Personalproject.entity.*;
import com.solo.Personalproject.repository.*;



import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;
    private  final PaymentRepository paymentRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount());
        orderItemList.add(orderItem);
        Payment payment = new Payment();
        Order order = Order.createOrder(member, orderItemList, payment);

        payment.setPaymentUid(order.getOrderUid());
        payment.setPrice(order.getPrice());
        payment.setStatus(PaymentStatus.OK);

        paymentRepository.save(payment);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);
        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        // Order -> OrderHistDto
        // OrderItem -> OrderItemDto
        for (Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(),
                        "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(),savedMember.getEmail())) {
            return false;
        }
        return true;
    }
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.orderCancel();
    }
    public Optional<Order> orderfind(String email){
        Member member = memberRepository.findByEmail(email);
        Optional<Order> order = orderRepository.findById(member.getId());
        return order;
    }

    public Long orders(List<OrderDto> orderDtoList, String email) {

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {  // 루프를 추가해 orderDtoList의 각 항목을 순회
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);
            System.out.println(item);
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // orderDto 사용
            orderItemList.add(orderItem); // 오더 아이템을 오더 리스트에 추가
        }

        Payment payment = new Payment();
        Order order = Order.createOrder(member, orderItemList, payment);

        payment.setPaymentUid(order.getOrderUid());
        payment.setPrice(order.getPrice());
        payment.setStatus(PaymentStatus.OK);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return order.getId();
    }
    public  Order  orderUidOrderCancle(String orderUid){

        System.out.println(orderUid);
        Order order=orderRepository.findByOrderUid(orderUid);

        order.orderCancel();
        Payment payment = paymentRepository.findById(order.getPayment().getId()).orElseThrow();
        payment.setStatus(PaymentStatus.CANCEL);

        return order;
    }
}
