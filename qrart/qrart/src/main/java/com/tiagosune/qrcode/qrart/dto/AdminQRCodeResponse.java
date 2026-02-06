package com.tiagosune.qrcode.qrart.dto;

import lombok.Data;

@Data
public class AdminQRCodeResponse {

    private Long id;
    private String title;
    private boolean paid;
    private boolean deleted;

    private Long userId;
    private String userEmail;
}

