package com.tiagosune.qrcode.qrart.service;

import com.tiagosune.qrcode.qrart.dto.AdminQRCodeResponse;
import com.tiagosune.qrcode.qrart.dto.AdminUserResponse;
import com.tiagosune.qrcode.qrart.model.QRCode;
import com.tiagosune.qrcode.qrart.repository.QrCodeRepository;
import com.tiagosune.qrcode.qrart.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsersRepository usersRepository;
    private final QrCodeRepository qrCodeRepository;

    public List<AdminUserResponse> getAllUsers() {
        return usersRepository.findAll().stream().map(user -> {
            AdminUserResponse dto = new AdminUserResponse();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            return dto;
        }).toList();
    }

    public List<AdminQRCodeResponse> getAllQRCodes() {
        return qrCodeRepository.findAll().stream().map(qr -> {
            AdminQRCodeResponse dto = new AdminQRCodeResponse();
            dto.setId(qr.getId());
            dto.setTitle(qr.getTitle());
            dto.setPaid(qr.isPaid());
            dto.setDeleted(qr.isDeleted());
            dto.setUserId(qr.getUser().getId());
            dto.setUserEmail(qr.getUser().getEmail());
            return dto;
        }).toList();
    }

    // soft delete forçado
    public void forceDeleteQRCode(Long id) {
        QRCode qr = qrCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QR Code não encontrado"));

        qr.setDeleted(true);
        qrCodeRepository.save(qr);
    }
}

