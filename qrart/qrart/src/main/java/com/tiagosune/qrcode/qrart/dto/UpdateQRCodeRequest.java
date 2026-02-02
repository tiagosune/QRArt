package com.tiagosune.qrcode.qrart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateQRCodeRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String text;
}
