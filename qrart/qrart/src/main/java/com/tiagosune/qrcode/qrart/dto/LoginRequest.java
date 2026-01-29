package com.tiagosune.qrcode.qrart.dto;


import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
