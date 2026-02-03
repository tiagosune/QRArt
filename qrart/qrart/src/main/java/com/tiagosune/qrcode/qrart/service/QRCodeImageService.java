package com.tiagosune.qrcode.qrart.service;


import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeImageService {

    private static final int QR_CODE_SIZE = 300;
    private static final int LOGO_SIZE = 90;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/qrcodes/";

    private BufferedImage generateQRCode(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // configuração para deixar a correção de erro no maximo
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = qrCodeWriter.encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
        );

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
        BufferedImage combinedImage = new BufferedImage(
                qrCode.getWidth(),
                qrCode.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = combinedImage.createGraphics();

        g2d.drawImage(qrCode, 0, 0, null);

        int x = (qrCode.getWidth() - logo.getWidth()) / 2;
        int y = (qrCode.getHeight() - logo.getHeight()) / 2;

        // adicionar borda branca ao redor da logo para ficar mais legivel
        int borderSize = 10;
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(
                x - borderSize,
                y - borderSize,
                logo.getWidth() + (borderSize * 2),
                logo.getHeight() + (borderSize * 2),
                20, // deixar os cantos arredondados
                20
        );

        g2d.drawImage(logo, x, y, null);

        g2d.dispose();
        return combinedImage;
    }


    private String saveImage(BufferedImage image, Long userId, Long qrCodeId) throws IOException {
        String userDir = UPLOAD_DIR + userId + "/";
        Path path = Paths.get(userDir);
        Files.createDirectories(path);

        String fileName = "qr_" + qrCodeId + ".png";
        Path filePath = Paths.get(userDir + fileName);
        ImageIO.write(image, "png", filePath.toFile());
        return "/uploads/qrcodes/" + userId + "/" + fileName;
    }

    public String generateAndSave(Long qrCodeId,
                                  Long userId,
                                  String text,
                                  MultipartFile logoFile)
            throws IOException, WriterException {

        if (logoFile != null && !logoFile.isEmpty()) {
            String contentType = logoFile.getContentType();
            if (contentType == null || !contentType.matches("image/(png|jpeg|jpg)")) {
                throw new IOException("Apenas imagens PNG ou JPG são permitidas");
            }

            long maxSize = 5 * 1024 * 1024; // 5MB
            if (logoFile.getSize() > maxSize) {
                throw new IOException("Imagem muito grande. Tamanho máximo: 5MB");
            }

            String originalFilename = logoFile.getOriginalFilename();
            if (originalFilename == null || originalFilename.contains("..")) {
                throw new IOException("Nome de arquivo inválido");
            }
        }

        BufferedImage qrCode = generateQRCode(text, QR_CODE_SIZE, QR_CODE_SIZE);
        BufferedImage logo = logoFile == null || logoFile.isEmpty()
                ? null
                : resizeLogo(ImageIO.read(logoFile.getInputStream()), LOGO_SIZE);
        BufferedImage combinedImage = logo == null ? qrCode : overlayLogo(qrCode, logo);
        return saveImage(combinedImage, userId, qrCodeId);
    }


}
