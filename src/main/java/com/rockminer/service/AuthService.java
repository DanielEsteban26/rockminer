package com.rockminer.service;

import com.rockminer.dto.LoginRequest;
import com.rockminer.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
