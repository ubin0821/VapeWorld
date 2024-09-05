package com.solo.Personalproject.repository;

import com.solo.Personalproject.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.member.email = :email order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);







    //포트 원
    @Query("select o from Order o" + //order와
            " left join fetch o.payment p" + //payment 조인. order를 따른다.
            " left join fetch o.member m" + //멤버와 조인, order를 따른다.
            " where o.id = :id") //order에 있는 주문번호 순으로 나열한다.
    Optional<Order> findOrderAndPaymentAndMember(Long id);

    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " where o.orderUid = :orderUid")
    Optional<Order> findOrderAndPayment(String orderUid);

    Order findByOrderUid(String orderUid);

}
