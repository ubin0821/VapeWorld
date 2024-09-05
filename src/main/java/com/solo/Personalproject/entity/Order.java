package com.solo.Personalproject.entity;

import com.solo.Personalproject.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int price;//총 주문 가격


    private String orderUid; // 주문 번호

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // private LocalDateTime regTime;

    // private LocalDateTime updateTime;

    //주문서 주문아이템 리스트에 주문 아이템 추가
    //주문 아이템에 주문서 추가
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    //주문서 생성
    // 현재 로그인 된 멤버 주문서에 추가
    // 주문아이템 리스트를 반복문을 통해서 주문서에 추가
    // 상태는 주문으로 세팅
    // 주문 시간은 현재시간으로 세팅
    // 주문서 리턴

    public static Order createOrder(Member member, List<OrderItem>orderItemList, Payment payment){
        Order order = new Order();
        order.setOrderUid(UUID.randomUUID().toString());
        order.setMember(member);
        order.setPayment(payment);

        for (OrderItem orderItem : orderItemList) {
            order.price += (orderItem.getOrderPrice() * orderItem.getCount());
            System.out.println(order.price);
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 주문서에 있는 주문 아이템 리스트를 반복
    // 주문 아이템마다 총 가격을 totalPrice에 추가
    public  int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void orderCancel(){
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
    public void  updateOrders(int amount){
        this.price =amount;
    }
}
