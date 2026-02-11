package com.tiagosune.qrcode.qrart.controller;

import com.tiagosune.qrcode.qrart.dto.AdminQRCodeResponse;
import com.tiagosune.qrcode.qrart.dto.AdminUserResponse;
import com.tiagosune.qrcode.qrart.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public List<AdminUserResponse> getUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/qrcodes")
    public List<AdminQRCodeResponse> getQRCodes() {
        return adminService.getAllQRCodes();
    }

    @DeleteMapping("/qrcodes/{id}")
    public void deleteQRCode(@PathVariable Long id) {
        adminService.forceDeleteQRCode(id);
    }
}

