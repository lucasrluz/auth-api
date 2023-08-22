package com.api.authapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.authapi.dtos.SignInDTORequest;
import com.api.authapi.dtos.SignInDTOResponse;
import com.api.authapi.dtos.SignUpDTORequest;
import com.api.authapi.dtos.SignUpDTOResponse;
import com.api.authapi.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpDTORequest signUpDTORequest) {
        try {
            SignUpDTOResponse signUpDTOResponse = this.userService.signup(signUpDTORequest); 
            
            return ResponseEntity.status(HttpStatus.CREATED).body(signUpDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody SignInDTORequest signInDTORequest) {
        try {
            SignInDTOResponse signInDTOResponse = this.userService.signin(signInDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(signInDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
