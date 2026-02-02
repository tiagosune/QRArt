package com.tiagosune.qrcode.qrart.controller;


import com.tiagosune.qrcode.qrart.dto.QRCodeResponse;
import com.tiagosune.qrcode.qrart.dto.UpdateQRCodeRequest;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.model.Users;
import com.tiagosune.qrcode.qrart.service.QRCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.tiagosune.qrcode.qrart.dto.CreateQRCodeRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QRCodeService qrCodeService;

    @PostMapping("/create")
    public QRCodeResponse createQRCodeForUser(@RequestBody @Valid CreateQRCodeRequest request) {
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
        response.setCreatedAt(qr.getCreatedAt());
        return response;
    }

    @GetMapping
    public List<QRCodeResponse> listForUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();

        List<QRCode> qrs = qrCodeService.listForUser(user);

        List<QRCodeResponse> response = new ArrayList<>();
        for (QRCode qrCode : qrs) {
            QRCodeResponse qrCodeResponse = new QRCodeResponse();
            qrCodeResponse.setId(qrCode.getId());
            qrCodeResponse.setTitle(qrCode.getTitle());
            qrCodeResponse.setText(qrCode.getText());
            qrCodeResponse.setPaid(qrCode.isPaid());
            qrCodeResponse.setImgPath(qrCode.getImgPath());
            qrCodeResponse.setCreatedAt(qrCode.getCreatedAt());
            response.add(qrCodeResponse);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public void deleteForUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();
        qrCodeService.deleteForUser(id, user);
    }

    @PutMapping("/{id}")
    public QRCodeResponse updateForUser(@PathVariable Long id, @RequestBody @Valid UpdateQRCodeRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();

        QRCode qr = qrCodeService.updateForUser(id, user, request.getTitle(), request.getText());
        QRCodeResponse response = new QRCodeResponse();
        response.setId(qr.getId());
        response.setTitle(qr.getTitle());
        response.setText(qr.getText());
        response.setPaid(qr.isPaid());
        response.setImgPath(qr.getImgPath());
        response.setCreatedAt(qr.getCreatedAt());
        return response;
    }
}
