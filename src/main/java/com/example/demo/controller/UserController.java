package com.example.demo.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DTOs.UserDto;
import com.example.demo.models.DTOs.converter.UserDtoConverter;
import com.example.demo.models.entity.enums.Role;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserDtoConverter userDtoConverter;
    private final UserService userService;

    public UserController (UserDtoConverter userDtoConverter ,UserService userService) {
        this.userDtoConverter = userDtoConverter;
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllUser() {
        Set<UserDto> userDtos = userDtoConverter.convert(userService.getAllUser());
        if (!userDtos.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "Found all users", userDtos));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not_found", "No user available", null));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseObject> authenticateUser(@RequestParam String username, 
                                                           @RequestParam String password, 
                                                           @RequestParam Role role) {
        try {
            Object userDetails = userService.authenticate(username, password, role);
            return ResponseEntity.ok(new ResponseObject("success", "Authentication successful", userDetails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            Boolean isChanged = userService.changePassword(username, oldPassword, newPassword);
            if (isChanged) {
                return ResponseEntity.ok(new ResponseObject("success", "Password changed successfully", null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseObject("error", "Password change failed", null));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), null));
        }
    }
}
