package com.tiagosune.qrcode.qrart.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QRCode {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean paid;
    private String imgPath;
    @ManyToOne
    Users users;

}
