package com.tiagosune.qrcode.qrart.controller;

import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final QrCodeRepository qrCodeRepository;

    @GetMapping("/r/{id}")
    public void redirect(
            @PathVariable Long id,
            HttpServletResponse response
    ) throws IOException {

        QRCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QR não encontrado"));

        if (!qrCode.isPaid()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.sendRedirect(qrCode.getText());
    }
}
