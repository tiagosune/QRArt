package com.tiagosune.qrcode.qrart.repository;

import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QRCode, Long> {

    List<QRCode> findAllByUser(Users user);
    Optional<QRCode> findByIdAndUser(Long id, Users user);

}
