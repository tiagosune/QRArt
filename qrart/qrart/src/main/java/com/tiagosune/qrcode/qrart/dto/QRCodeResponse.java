package com.tiagosune.qrcode.qrart.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QRCodeResponse {

    private Long id;
    private String title;
    private String text;
    private String imgPath;
    private boolean paid;
    private LocalDateTime createdAt;

}
