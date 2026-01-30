package com.tiagosune.qrcode.qrart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class QRCode {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String text;
    private boolean paid;
    private String imgPath;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;
}
