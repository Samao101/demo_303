package com.example.demo.Repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>{
    @Query("SELECT cc FROM Teacher cc")
    Set<Teacher> findAlls();

    Optional<Teacher> findByEmail(String email);

    Set<Teacher> findByName(String name);

    Set<Teacher> findByMainCourseId(String courseId);

    Set<Teacher> findByIsAvailable(Boolean isAvailable);

    Set<Teacher> findByMainCourseIdAndIsAvailable(String courseId, Boolean isAvailable);

    boolean existsByCitizenIdentification(String citizenIdentification);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Teacher> findByCitizenIdentification(String citizenIdentification);

    Optional<Teacher> findByUserId(Long id);
}
