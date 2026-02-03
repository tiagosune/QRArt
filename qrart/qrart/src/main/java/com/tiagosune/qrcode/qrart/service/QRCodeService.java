package com.tiagosune.qrcode.qrart.service;

import com.tiagosune.qrcode.qrart.dto.QRCodeResponse;
import com.tiagosune.qrcode.qrart.exception.QrNotFoundException;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;

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

    public void deleteForUser(Long id, Users user) {
        QRCode qrCode = qrCodeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new QrNotFoundException("QR não encontrado para esse usuário"));
        qrCodeRepository.delete(qrCode);
    }

    public QRCode updateForUser(Long id, Users user, String title, String text) {
        QRCode qrCode = qrCodeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new QrNotFoundException("QR não encontrado para esse usuário"));
        qrCode.setTitle(title.trim());
        qrCode.setText(text.trim());
        return qrCodeRepository.save(qrCode);
    }
}
