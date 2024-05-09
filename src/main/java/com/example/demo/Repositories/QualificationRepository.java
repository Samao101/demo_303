package com.example.demo.Repositories;


import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.Qualification;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    @Query("SELECT cc FROM Qualification cc")
    Set<Qualification> findAlls();

    Set<Qualification> findByTeacherId(Long teacherId);
}
