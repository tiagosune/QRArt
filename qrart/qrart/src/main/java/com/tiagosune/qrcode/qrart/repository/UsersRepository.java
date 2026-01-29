package com.tiagosune.qrcode.qrart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tiagosune.qrcode.qrart.model.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository <Users, Long>{

    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

}
