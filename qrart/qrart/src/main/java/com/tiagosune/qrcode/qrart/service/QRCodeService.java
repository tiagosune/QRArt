package com.tiagosune.qrcode.qrart.service;


import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QrCodeRepository qrCodeRepository;

    public QRCode createForUser (Users user, String title, String text) {
        if (user != null) {
            QRCode newQRCode = new QRCode();
            if (title != null && !title.isBlank()){
                newQRCode.setTitle(title.trim());
            } else {throw new RuntimeException("Titulo não pode ser nulo ao criar um QRCode");}
            if (text != null && !text.isBlank()){
                newQRCode.setText(text.trim());
            } else {throw new RuntimeException("URL não pode ser nulo ao criar um QRCode");}
            newQRCode.setUser(user);
            newQRCode.setPaid(false);
            newQRCode.setImgPath(null);
            return qrCodeRepository.save(newQRCode);
        }
        throw new RuntimeException("Usuário não pode ser nulo ao criar um QRCode");
    }
}
