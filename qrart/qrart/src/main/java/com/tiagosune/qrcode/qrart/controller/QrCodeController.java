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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QRCodeService qrCodeService;

    @PostMapping("/create")
    public QRCodeResponse createQRCodeForUser(@RequestParam("title") String title,
                                              @RequestParam("text") String text,
                                              @RequestParam(value = "logoFile", required = false) MultipartFile logoFile) {

        if(title == null || title.isBlank()) {
            throw new RuntimeException("Titulo não pode ser vazio");
        }
        if(text == null || text.isBlank()) {
            throw new RuntimeException("Texto não pode ser vazio");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();

        QRCode qr = qrCodeService.createForUser(user, title, text, logoFile);
        return toResponse(qr);
    }

    @GetMapping
    public List<QRCodeResponse> listForUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();

        List<QRCode> qrs = qrCodeService.listForUser(user);

        List<QRCodeResponse> response = new ArrayList<>();
        for (QRCode qrCode : qrs) {
            QRCodeResponse tempObject = toResponse(qrCode);
            response.add(tempObject);
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
        return toResponse(qr);
    }

    @GetMapping("/{id}")
    public QRCodeResponse getById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) auth.getPrincipal();

        QRCode qr = qrCodeService.getByIdAndUser(id, user);
        return toResponse(qr);
    }



    private QRCodeResponse toResponse(QRCode qr) {
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
