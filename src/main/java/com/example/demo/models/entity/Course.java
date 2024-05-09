package com.example.demo.models.entity;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.JoinColumn;

@Entity
@Table
public class Course {
    @Id
    @Column(unique = true) // Đảm bảo mã môn học là duy nhất
    private String courseId;

    @NotBlank
    @Column(unique = true)
    private String courseName;
    
    @NotEmpty(message = "Class days must not be empty")
    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "course_days", joinColumns = @JoinColumn(name = "course_id"))
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> classDays= new HashSet<>();

    @NotNull
    @Positive
    private Integer maxRegistPerRegist;

    @NotNull
    @Positive
    private Integer maxStuPerClass;

    @NotNull
    private Boolean isAvailable;

    public Course() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Set<DayOfWeek> getClassDays() {
        return this.classDays;
    }

    public void addClassDays(DayOfWeek classDay) {
        this.classDays.add(classDay);
    }

    public void setClassDays(Set<DayOfWeek> classDays) {
        this.classDays = classDays;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getMaxRegistPerRegist() {
        return maxRegistPerRegist;
    }

    public void setMaxRegistPerRegist(Integer maxRegistPerRegist) {
        this.maxRegistPerRegist = maxRegistPerRegist;
    }

    public Integer getMaxStuPerClass() {
        return maxStuPerClass;
    }

    public void setMaxStuPerClass(Integer maxStuPerClass) {
        this.maxStuPerClass = maxStuPerClass;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
