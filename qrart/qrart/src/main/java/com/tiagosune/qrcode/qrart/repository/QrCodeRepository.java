package com.tiagosune.qrcode.qrart.repository;

import com.tiagosune.qrcode.qrart.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QRCode, Long> {
}
