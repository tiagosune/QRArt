package com.tiagosune.qrcode.qrart.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Users users;
    @ManyToOne
    private QRCode qrcode;

}
