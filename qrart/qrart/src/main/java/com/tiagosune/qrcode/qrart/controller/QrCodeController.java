package com.tiagosune.qrcode.qrart.controller;

import com.tiagosune.qrcode.qrart.services.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerateQrResponseDTO> generateQrCode (
            @RequestParam("text") String text,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        GenerateQrResponseDTO response = qrCodeService.generate(text, image, color, size);

        return ResponseEntity.ok(response);
    }


}
