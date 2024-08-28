package com.solo.Personalproject.repository;


import com.solo.Personalproject.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentUid(String PaymentUid);
}
