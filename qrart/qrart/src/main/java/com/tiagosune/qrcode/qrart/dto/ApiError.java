package com.tiagosune.qrcode.qrart.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class ApiError {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
