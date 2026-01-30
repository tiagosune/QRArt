package com.tiagosune.qrcode.qrart.service;


import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QrCodeRepository qrCodeRepository;

    public QRCode createForUser(Users user, String title, String text) {
        if (user != null) {
            QRCode newQRCode = new QRCode();
            newQRCode.setTitle(title.trim());
            newQRCode.setText(text.trim());
            newQRCode.setUser(user);
            newQRCode.setPaid(false);
            newQRCode.setImgPath(null);
            newQRCode.setCreatedAt(java.time.LocalDateTime.now());
            return qrCodeRepository.save(newQRCode);
        }
        throw new RuntimeException("Usuário não pode ser nulo ao criar um QRCode");
    }

    public List<QRCode> listForUser(Users user) {
        return qrCodeRepository.findAllByUser(user);
    }
}
