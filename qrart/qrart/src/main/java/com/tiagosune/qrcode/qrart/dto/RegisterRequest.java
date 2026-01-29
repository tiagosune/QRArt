package com.tiagosune.qrcode.qrart.dto;

import lombok.Data;


@Data

public class RegisterRequest {

    private String name;
    private String email;
    private String password;

}
