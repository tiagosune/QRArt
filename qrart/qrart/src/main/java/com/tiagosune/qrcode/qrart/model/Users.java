package com.tiagosune.qrcode.qrart.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role = "USER";

}
