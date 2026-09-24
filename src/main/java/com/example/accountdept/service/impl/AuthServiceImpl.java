package com.example.accountdept.service.impl;

import com.example.accountdept.dto.*;
import com.example.accountdept.entity.*;
import com.example.accountdept.exception.ApiException;
import com.example.accountdept.repository.AccountRepository;
import com.example.accountdept.service.AuthService;
import com.example.accountdept.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {	
    private final  AccountRepository accountRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final com.example.accountdept.repository.PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public String login(LoginRequest request) {
        Account acc = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("BAD_CREDENTIALS"));
        if (!passwordEncoder.matches(request.getPassword(), acc.getPassword())) throw new ApiException("BAD_CREDENTIALS"); // Sửa passwordHash thành password
        if (!Boolean.TRUE.equals(acc.getActive())) throw new ApiException("ACCOUNT_INACTIVE");
        return jwtTokenProvider.generateToken(acc.getUsername(), acc.getRole().name(), request.getRememberMe() != null && request.getRememberMe());
    }

    @Transactional
    @Override
    public void register(RegisterRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) throw new ApiException("USERNAME_EXISTS");
        if (accountRepository.existsByEmail(request.getEmail())) throw new ApiException("EMAIL_EXISTS");
        Account acc = Account.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Sửa passwordHash thành password
                .role(Role.EMPLOYEE)
                .active(true)
                .createdDate(Instant.now())
                .build();
        accountRepository.save(acc);
    }


    @Transactional
    @Override
    public void forgotPassword(String email) {
        // Không tiết lộ email có tồn tại hay không: luôn trả về thành công.
        if (accountRepository.findByEmail(email).isEmpty()) return;
        passwordResetTokenRepository.deleteByEmail(email);
        String token = java.util.UUID.randomUUID().toString();
        passwordResetTokenRepository.save(PasswordResetToken.builder()
                .email(email)
                .token(token)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build());
        // Demo: chưa gửi email thật, token được ghi vào log để test (trong thực tế gửi qua email).
        log.info("Password reset token for {}: {}", email, token);
    }

    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken t = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException("INVALID_TOKEN"));
        if (t.getExpiryDate().isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(t);
            throw new ApiException("TOKEN_EXPIRED");
        }
        Account acc = accountRepository.findByEmail(t.getEmail())
                .orElseThrow(() -> new ApiException("ACCOUNT_NOT_FOUND"));
        acc.setPassword(passwordEncoder.encode(newPassword));
        passwordResetTokenRepository.delete(t);
    }

    @Transactional
    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        Account acc = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("ACCOUNT_NOT_FOUND"));
        if (!passwordEncoder.matches(oldPassword, acc.getPassword())) throw new ApiException("BAD_CREDENTIALS"); // Sửa passwordHash thành password
        acc.setPassword(passwordEncoder.encode(newPassword)); // Sửa setPasswordHash thành setPassword
    }
}
