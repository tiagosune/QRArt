package com.tiagosune.qrcode.qrart.dto;

import lombok.Data;

@Data

public class AuthResponse {

    private String name;
    private String email;
    private String token;

}
