package com.example.demo.Repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    @Query("SELECT cc FROM Student cc")
    Set<Student> findAlls();

    Set<Student> findByName(String name);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByCitizenIdentification(String citizenIdentification);

    Optional<Student> findByPhoneNumber(String phoneNumber);

    boolean existsByCitizenIdentification(String citizenIdentification);

    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Student> findByUserId(Long id);
}
