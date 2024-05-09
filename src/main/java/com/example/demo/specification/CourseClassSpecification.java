package com.example.demo.specification;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.enums.CourseLevel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;

@Component
public class CourseClassSpecification {

    public static Specification<CourseClass> findByCriteria(String courseId, CourseLevel courseLevel, DayOfWeek dayOfWeek) {
        return (Root<CourseClass> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<CourseClass, Course> courseJoin = root.join("course");

            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(builder.equal(courseJoin.get("courseId"), courseId));
            }
            if (courseLevel != null) {
                predicates.add(builder.equal(root.get("courseLevel"), courseLevel));
            }
            if (dayOfWeek != null) {
                predicates.add(builder.equal(root.get("dayOfWeek"), dayOfWeek));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
