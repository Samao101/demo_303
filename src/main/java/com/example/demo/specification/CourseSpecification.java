package com.example.demo.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.demo.models.entity.Course;
import jakarta.persistence.criteria.Predicate;

@Component
public class CourseSpecification {

    public static Specification<Course> findByCriteria(String courseId, String courseName, Boolean isAvailable) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("courseId"), courseId));
            }
            if (courseName != null && !courseName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("courseName")), "%" + courseName.toLowerCase() + "%"));
            }

            if (isAvailable != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("isAvailable")), "%" + isAvailable+ "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
