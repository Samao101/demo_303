package com.example.demo.Repositories;

import java.time.DayOfWeek;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long>, JpaSpecificationExecutor<CourseClass>{
       
       Set<CourseClass> findByCourseId(String courseId);

       Set<CourseClass> findByCourseIdAndCourseLevel(String courseId, CourseLevel courseLevel);

       @Query("SELECT cc FROM CourseClass cc WHERE cc.dayOfWeek = :dayOfWeek")
       Set<CourseClass> findByDayOfWeek(@Param("dayOfWeek") DayOfWeek dayOfWeek);


       @Query("SELECT cc FROM CourseClass cc " +
            "WHERE :studentId MEMBER OF cc.studentIds " +
            "AND cc.courseId = :courseId " +
            "AND cc.courseLevel = :courseLevel " +
            "AND cc.courseClassStatus = :status")
       Set<CourseClass> findCourseClassesByStudentIdAndCourseIdAndCourseLevelAndStatus(
            @Param("studentId") Long studentId,
            @Param("courseId") String courseId,
            @Param("courseLevel") CourseLevel courseLevel,
            @Param("status") CourseClassStatus status
       );

       @Query("SELECT cc FROM CourseClass cc WHERE cc.teacherId = :teacherId AND cc.courseClassStatus = :courseClassStatus AND " +
              "cc.dayOfWeek = :dayOfWeek AND " +
              "((cc.startPeriod <= :endPeriod AND cc.endPeriod >= :endPeriod) OR " +
              "(cc.startPeriod <= :startPeriod AND cc.endPeriod >= :startPeriod))")
       Set<CourseClass> findOverlappingClassesForTeacher(@Param("teacherId") Long teacherId,
                                                          @Param("courseClassStatus") CourseClassStatus courseClassStatus,
                                                          @Param("dayOfWeek") DayOfWeek dayOfWeek,
                                                          @Param("startPeriod") Integer startPeriod, 
                                                          @Param("endPeriod") Integer endPeriod);

       @Query("SELECT cc FROM CourseClass cc JOIN cc.studentIds studentId WHERE studentId = :studentId AND cc.courseClassStatus = :courseClassStatus AND " +
              "cc.dayOfWeek = :dayOfWeek AND " +
              "((cc.startPeriod <= :endPeriod AND cc.endPeriod >= :endPeriod) OR " +
              "(cc.startPeriod <= :startPeriod AND cc.endPeriod >= :startPeriod))")
       Set<CourseClass> findOverlappingClassesForStudent(@Param("studentId") Long studentId,
                                                          @Param("courseClassStatus") CourseClassStatus courseClassStatus,
                                                          @Param("dayOfWeek") DayOfWeek dayOfWeek,
                                                          @Param("startPeriod") Integer startPeriod, 
                                                          @Param("endPeriod") Integer endPeriod);

       Set<CourseClass> findByCourseClassStatus(CourseClassStatus courseClassStatus);
       
       Set<CourseClass> findByStudentIdsContainsAndDayOfWeekAndCourseClassStatus(Long studentId, 
                                                                                  DayOfWeek dayOfWeek, 
                                                                                  CourseClassStatus courseClassStatus);
       
       Set<CourseClass> findByStudentIdsContainsAndCourseClassStatus( Long studentId, 
                                                                      CourseClassStatus courseClassStatus);

       Set<CourseClass> findByStudentIdsContains(Long studentId);

       Set<CourseClass> findByTeacherId(Long teacherId);

       Set<CourseClass> findByTeacherIdAndCourseClassStatus(Long teacherId, CourseClassStatus courseClassStatus);
}   

