package com.example.demo.specification;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.models.entity.CourseToRegistration;
import com.example.demo.models.entity.enums.CourseLevel;

import jakarta.persistence.criteria.Predicate;

public class CourseToRegistrationSpecification {
    public static Specification<CourseToRegistration> findByCriteria(String courseId, CourseLevel courseLevel, DayOfWeek dayOfWeek) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (courseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("course").get("courseId"), courseId));
            }
            if (courseLevel != null) {
                predicates.add(criteriaBuilder.equal(root.get("courseLevel"), courseLevel));
            }
            if (dayOfWeek != null) {
                predicates.add(criteriaBuilder.equal(root.get("dayOfWeek"), dayOfWeek));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
