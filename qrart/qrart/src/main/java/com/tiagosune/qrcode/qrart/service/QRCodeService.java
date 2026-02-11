package com.tiagosune.qrcode.qrart.service;

import com.google.zxing.WriterException;
import com.tiagosune.qrcode.qrart.exception.QrNotFoundException;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final QRCodeImageService qrCodeImageService;

    @Value("${app.base-url}")
    private String baseUrl;

    public QRCode createForUser(Users user, String title, String text, MultipartFile file) {
        if (user == null) {
            throw new RuntimeException("Usuário não pode ser nulo");
        }

        QRCode qrCode = new QRCode();
        qrCode.setTitle(title.trim());
        qrCode.setText(text.trim()); // aqui fica o link real
        qrCode.setUser(user);

        qrCode.setPaid("ROLE_ADMIN".equals(user.getRole()));
        qrCode.setCreatedAt(LocalDateTime.now());

        qrCode = qrCodeRepository.save(qrCode);

        try {
            String dynamicUrl = baseUrl + "/r/" + qrCode.getId();

            String imgPath = qrCodeImageService.generateAndSave(
                    qrCode.getId(),
                    user.getId(),
                    dynamicUrl,
                    file
            );

            qrCode.setImgPath(imgPath);
            return qrCodeRepository.save(qrCode);

        } catch (IOException | WriterException e) {
            qrCodeRepository.delete(qrCode);
            throw new RuntimeException("Erro ao gerar QR Code");
        }
    }

    public List<QRCode> listForUser(Users user) {
        return qrCodeRepository.findAllByUserAndDeletedFalse(user);
    }

    public QRCode getByIdAndUser(Long id, Users user) {
        return qrCodeRepository
                .findByIdAndUserAndDeletedFalse(id, user)
                .orElseThrow(() -> new QrNotFoundException("QR Code não encontrado"));
    }

    public QRCode updateForUser(Long id, Users user, String title, String text) {
        QRCode qrCode = getByIdAndUser(id, user);
        qrCode.setTitle(title.trim());
        qrCode.setText(text.trim());
        return qrCodeRepository.save(qrCode);
    }

    public void deleteForUser(Long id, Users user) {
        QRCode qrCode = getByIdAndUser(id, user);
        qrCode.setDeleted(true);
        qrCodeRepository.save(qrCode);
    }
}
