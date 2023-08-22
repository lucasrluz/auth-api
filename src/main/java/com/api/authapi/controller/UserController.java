package com.api.authapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.authapi.dtos.UpdateUserDTORequest;
import com.api.authapi.dtos.UpdateUserDTOResponse;
import com.api.authapi.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody UpdateUserDTORequest updateUserDTORequest, Authentication authentication) {
        try {
            String userId = authentication.getName();

            UpdateUserDTOResponse updateUserDTOResponse = this.userService.update(updateUserDTORequest, userId);

            return ResponseEntity.status(HttpStatus.OK).body(updateUserDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
