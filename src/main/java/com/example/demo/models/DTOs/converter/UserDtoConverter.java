package com.example.demo.models.DTOs.converter;

import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.UserDto;
import com.example.demo.models.entity.User;

@Component
public class UserDtoConverter {
    public UserDto convert (User from) {
        return new UserDto(
            from.getId(),
            from.getUsername(),
            from.getPassword(),
            from.getRole()
        );
    }

    public Set<UserDto> convert(Set<User> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<UserDto> convert(Optional<User> from) {
        return from.map(this::convert);
    }
}
