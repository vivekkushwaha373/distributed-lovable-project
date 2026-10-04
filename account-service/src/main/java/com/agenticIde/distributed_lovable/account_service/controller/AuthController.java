package com.agenticIde.distributed_lovable.account_service.controller;

import com.agenticIde.distributed_lovable.account_service.dto.auth.AuthResponse;
import com.agenticIde.distributed_lovable.account_service.dto.auth.LoginRequest;
import com.agenticIde.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.agenticIde.distributed_lovable.account_service.dto.auth.UserProfileResponse;
import com.agenticIde.distributed_lovable.account_service.service.AuthService;
//import com.openai.services.blocking.admin.organization.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AuthController {
    private AuthService authService;
//    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signup(@RequestBody LoginRequest request){
        log.info("🔥 LOGIN CONTROLLER REACHED");
        return ResponseEntity.ok(authService.login(request));
    }

//    @GetMapping("/me")
//    public ResponseEntity<UserProfileResponse> getProfile(){
//        Long userid = 1L;
//        return ResponseEntity.ok(userService.getProfile(userid));
//    }



}
