package com.tiagosune.qrcode.qrart.controller;


import com.tiagosune.qrcode.qrart.dto.QRCodeResponse;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tiagosune.qrcode.qrart.dto.CreateQRCodeRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QRCodeService qrCodeService;

    @PostMapping("/create")
    public QRCodeResponse createQRCodeForUser (@RequestBody CreateQRCodeRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();
        String title = request.getTitle();
        String text = request.getText();
        QRCode qr = qrCodeService.createForUser(user, title, text);
        QRCodeResponse response = new QRCodeResponse();
        response.setId(qr.getId());
        response.setTitle(qr.getTitle());
        response.setText(qr.getText());
        response.setPaid(qr.isPaid());
        response.setImgPath(qr.getImgPath());
        return response;
    }

}
