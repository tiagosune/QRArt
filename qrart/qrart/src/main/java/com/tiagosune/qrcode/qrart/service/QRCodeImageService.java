package com.tiagosune.qrcode.qrart.service;


import org.springframework.stereotype.Service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QRCodeImageService {

    private static final int QR_CODE_SIZE = 300;
    private static final int LOGO_SIZE = 90;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/qrcodes/";

    private BufferedImage generateQRCode(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private BufferedImage resizeLogo(BufferedImage originalLogo, int targetSize) {
        Image tmp = originalLogo.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH);
        BufferedImage resizedLogo = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedLogo.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resizedLogo;
    }

    private BufferedImage overlayLogo(BufferedImage qrCode, BufferedImage logo) {
        BufferedImage combinedImage = new BufferedImage(qrCode.getWidth(), qrCode.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(qrCode, 0, 0, null);
        g2d.drawImage(logo, (qrCode.getWidth() - logo.getWidth()) / 2, (qrCode.getHeight() - logo.getHeight()) / 2, null);
        g2d.dispose();
        return combinedImage;
    }

}
