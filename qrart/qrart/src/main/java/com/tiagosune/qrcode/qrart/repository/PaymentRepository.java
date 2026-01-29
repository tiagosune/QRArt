package com.tiagosune.qrcode.qrart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tiagosune.qrcode.qrart.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
