package com.example.demo.Repositories;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.Course;
@Repository
public interface CourseRepository extends JpaRepository<Course, String>, JpaSpecificationExecutor<Course> {
    @Query("SELECT cc FROM Course cc")
    Set<Course> findAlls();

    public Optional<Course> findByCourseId (String CourseID);

    Optional<Course> findByCourseName(String courseName);

    Set<Course> findByIsAvailable(Boolean isAvailable);
    
    @Query("SELECT c FROM Course c JOIN c.classDays d WHERE d = :dayOfWeek")
    Set<Course> findByClassDays(@Param("dayOfWeek") DayOfWeek dayOfWeek);
}
