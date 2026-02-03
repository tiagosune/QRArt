package com.tiagosune.qrcode.qrart.service;

import com.google.zxing.WriterException;
import com.tiagosune.qrcode.qrart.exception.QrNotFoundException;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final QRCodeImageService qrCodeImageService;

    public QRCode createForUser(Users user, String title, String text, MultipartFile file) {
        if (user == null) {
            throw new RuntimeException("Usuário não pode ser nulo ao criar um QRCode");
        }

        QRCode newQRCode = new QRCode();
        newQRCode.setTitle(title.trim());
        newQRCode.setText(text.trim());
        newQRCode.setUser(user);
        newQRCode.setPaid(false);
        newQRCode.setCreatedAt(java.time.LocalDateTime.now());
        newQRCode = qrCodeRepository.save(newQRCode);

        try {
            String imgPath = qrCodeImageService.generateAndSave(newQRCode.getId(),
                    newQRCode.getUser().getId(),
                    newQRCode.getText(),
                    file);

            newQRCode.setImgPath(imgPath);
            return qrCodeRepository.save(newQRCode);
        } catch (IOException | WriterException e) {
            qrCodeRepository.delete(newQRCode);
            throw new RuntimeException("Erro ao gerar imagem do QR Code: " + e.getMessage());
        }
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
