package com.agenticIde.distributed_lovable.account_service.service;

import com.agenticIde.distributed_lovable.account_service.dto.auth.AuthResponse;
import com.agenticIde.distributed_lovable.account_service.dto.auth.LoginRequest;
import com.agenticIde.distributed_lovable.account_service.dto.auth.SignupRequest;


import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}