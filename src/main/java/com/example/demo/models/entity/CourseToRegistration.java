package com.example.demo.models.entity;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import com.example.demo.models.entity.base.BaseEntityWithIdLong;
import com.example.demo.models.entity.enums.CourseLevel;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table
public class CourseToRegistration extends BaseEntityWithIdLong{
    
    @NotNull
    @NotBlank
    private String courseId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Positive
    @Min(1)
    @Max(18)
    @Column
    private Integer startPeriod;

    @Positive
    @Min(1)
    @Max(18)
    @Column
    private Integer endPeriod;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;



    @Positive
    private Integer maxRegist= 120;

    @Positive
    private Integer maxRegistClass= 40;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Long> studentIds = new HashSet<>();

   
    public void addStudentId(Student student) {
        this.studentIds.add(student.getId());
    }


    public String getCourseId() {
        return courseId;
    }


    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public CourseLevel getCourseLevel() {
        return courseLevel;
    }


    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }


    public Integer getStartPeriod() {
        return startPeriod;
    }


    public void setStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
    }


    public Integer getEndPeriod() {
        return endPeriod;
    }


    public void setEndPeriod(Integer endPeriod) {
        this.endPeriod = endPeriod;
    }


    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }


    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public Integer getMaxRegist() {
        return maxRegist;
    }


    public void setMaxRegist(Integer maxRegist) {
        this.maxRegist = maxRegist;
    }


    public Integer getMaxRegistClass() {
        return maxRegistClass;
    }


    public void setMaxRegistClass(Integer maxRegistClass) {
        this.maxRegistClass = maxRegistClass;
    }


    public Set<Long> getStudents() {
        return studentIds;
    }


    public void setStudents(Set<Long> studentIds) {
        this.studentIds = studentIds;
    }

   
}
