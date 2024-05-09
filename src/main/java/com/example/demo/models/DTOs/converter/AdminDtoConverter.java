package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.AdminDto;
import com.example.demo.models.entity.Admin;

@Component
public class AdminDtoConverter {
    public AdminDto convert(Admin from) {
        return new AdminDto(
                from.getId(),
                from.getName(),
                from.getAge(),
                from.getGender(),
                from.getDateOfBirth(),
                from.getPlaceOfBirth(),
                from.getCitizenIdentification(),
                from.getEmail(),
                from.getPhoneNumber()
        );
    }

    public Set<AdminDto> convert(Set<Admin> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<AdminDto> convert(Optional<Admin> from) {
        return from.map(this::convert);
    }
}
