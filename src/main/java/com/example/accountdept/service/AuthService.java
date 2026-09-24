package com.example.accountdept.service;

import com.example.accountdept.dto.*;

public interface AuthService {
    String login(LoginRequest request);
    void register(RegisterRequest request);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    void changePassword(String username, String oldPassword, String newPassword);
}
