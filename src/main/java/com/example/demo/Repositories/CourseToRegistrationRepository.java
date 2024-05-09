package com.example.demo.Repositories;

import java.time.DayOfWeek;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.CourseToRegistration;
import com.example.demo.models.entity.enums.CourseLevel;

@Repository
public interface CourseToRegistrationRepository extends JpaRepository<CourseToRegistration, Long>, JpaSpecificationExecutor<CourseToRegistration>{
    @Query("SELECT cc FROM CourseToRegistration cc")
    Set<CourseToRegistration> findAlls();

    Set<CourseToRegistration> findByCourseId(String courseId);

    Set<CourseToRegistration> findByCourseIdAndCourseLevel(String courseId, CourseLevel courseLevel);

    @Query("SELECT ctr FROM CourseToRegistration ctr WHERE ctr.courseId = :courseId AND ctr.startPeriod <= :period AND ctr.endPeriod >= :period AND ctr.dayOfWeek = :dayOfWeek")
    Set<CourseToRegistration> findByCourseIdAndPeriodAndDayOfWeek(@Param("courseId") String courseId, @Param("period") Integer period, @Param("dayOfWeek") DayOfWeek dayOfWeek);

    boolean existsByCourseIdAndCourseLevelAndStartPeriodAndEndPeriod(
            String courseId, CourseLevel courseLevel, Integer startPeriod, Integer endPeriod);

    @Query("SELECT c FROM CourseToRegistration c WHERE :studentId MEMBER OF c.studentIds")
    Set<CourseToRegistration> findAllByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT c FROM CourseToRegistration c WHERE :studentId MEMBER OF c.studentIds AND c.courseLevel = :courseLevel AND c.courseId = :courseId")
    Set<CourseToRegistration> findByStudentIdAndCourseLevelAndCourseId(
        @Param("studentId") Long studentId,
        @Param("courseLevel") CourseLevel courseLevel,
        @Param("courseId") String courseId);
}