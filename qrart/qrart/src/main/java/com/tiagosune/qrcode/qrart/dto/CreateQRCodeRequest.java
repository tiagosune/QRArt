package com.tiagosune.qrcode.qrart.dto;


import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateQRCodeRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String text;
}
