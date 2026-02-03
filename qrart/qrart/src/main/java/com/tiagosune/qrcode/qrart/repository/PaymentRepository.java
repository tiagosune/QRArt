package com.tiagosune.qrcode.qrart.repository;

import com.tiagosune.qrcode.qrart.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByStripeSessionId(String stripeSessionId);
}
