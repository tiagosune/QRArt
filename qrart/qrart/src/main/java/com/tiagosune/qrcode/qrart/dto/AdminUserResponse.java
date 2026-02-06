package com.tiagosune.qrcode.qrart.dto;

import lombok.Data;

@Data
public class AdminUserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
}

