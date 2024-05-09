package com.example.demo.models.DTOs;

import com.example.demo.models.entity.enums.Role;

public record UserDto(
    Long id,
    String username,
    String password,
    Role role
) {
} 