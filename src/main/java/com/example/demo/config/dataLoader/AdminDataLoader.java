package com.example.demo.config.dataLoader;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.request.Admin.CreateAdminRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class AdminDataLoader {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public AdminDataLoader(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public Set<CreateAdminRequest> loadAdminData() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:baseData/adminData.json");
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<Set<CreateAdminRequest>>() {});
    }
}