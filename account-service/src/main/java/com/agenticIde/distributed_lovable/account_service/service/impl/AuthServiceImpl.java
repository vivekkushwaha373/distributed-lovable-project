package com.agenticIde.distributed_lovable.account_service.service.impl;

import com.agenticIde.distributed_lovable.account_service.dto.auth.AuthResponse;
import com.agenticIde.distributed_lovable.account_service.dto.auth.LoginRequest;
import com.agenticIde.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.agenticIde.distributed_lovable.account_service.entity.User;
import com.agenticIde.distributed_lovable.account_service.mapper.UserMapper;
import com.agenticIde.distributed_lovable.account_service.repository.UserRepository;
import com.agenticIde.distributed_lovable.account_service.service.AuthService;
import com.agenticIde.distributed_lovable.comman_lib.error.BadRequestException;
import com.agenticIde.distributed_lovable.comman_lib.security.AuthUtil;
import com.agenticIde.distributed_lovable.comman_lib.security.JwtUserPrinciple;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;
    @Override
    public AuthResponse signup(SignupRequest request)
    {
        userRepository.findByUsername(request.username()).ifPresent((user) -> {
            throw new BadRequestException("User Already Exists");
        });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        JwtUserPrinciple jwtUserPrincipal = new JwtUserPrinciple(user.getId(), user.getName(),
                user.getUsername(), null,  new ArrayList<>());

        String jwttoken = authUtil.generateAccessToken(jwtUserPrincipal);
        return new AuthResponse(jwttoken,userMapper.toUserProfileResponse(jwtUserPrincipal));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("🔥 LOGIN SERVICE REACHED");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
//       User user = (User) authentication.getPrincipal();
//       log.trace("user:{}",user);
       JwtUserPrinciple user = (JwtUserPrinciple) authentication.getPrincipal();
       String jwttoken = authUtil.generateAccessToken(user);
       return new AuthResponse(jwttoken,userMapper.toUserProfileResponse(user));
    }
}
