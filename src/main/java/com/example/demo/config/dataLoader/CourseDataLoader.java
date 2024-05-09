package com.example.demo.config.dataLoader;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.request.Course.BaseCourseRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class CourseDataLoader {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public CourseDataLoader(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public Set<BaseCourseRequest> loadCourseData() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:baseData/courseData.json");
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<Set<BaseCourseRequest>>() {});
    }   
}