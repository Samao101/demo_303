package com.example.demo.models.entity;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import com.example.demo.models.entity.base.BaseEntityWithIdLong;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.validation.ValidPeriod;
import com.example.demo.validation.ValidCourseClassStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table
@ValidPeriod
public class CourseClass extends BaseEntityWithIdLong{
    String courseId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @NotNull
    @Positive
    @Min(1)
    @Max(18)
    @Column
    private Integer startPeriod;

    @NotNull
    @Positive
    @Min(1)
    @Max(18)
    @Column
    private Integer endPeriod;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    Long teacherId;

    @NotNull
    @Positive
    private Integer capacity;

    @Transient
    private Integer studentCount;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Long> studentIds = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @ValidCourseClassStatus(anyOf = {CourseClassStatus.inPlanned ,CourseClassStatus.inProgress, CourseClassStatus.Completed})
    private CourseClassStatus courseClassStatus;


    public void setStudentIds(Set<Long> studentIds) {
        this.studentIds = studentIds;
    }


    public CourseClass() {
        this.studentIds= new HashSet<>();
        this.courseClassStatus= CourseClassStatus.inPlanned;
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

    public void addStudentIds(Student student) {
        this.studentIds.add(student.getId());
    }


    public CourseClassStatus getCourseClassStatus() {
        return courseClassStatus;
    }

    public void setCourseClassStatus(CourseClassStatus courseClassStatus) {
        this.courseClassStatus = courseClassStatus;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public Integer getStudentCount() {
        return studentIds != null ? (studentIds.size()) : 0;
    }

    public void setStudentCount() {
        this.studentCount = studentIds != null ? studentIds.size() : 0;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Set<Long> getStudentIds() {
        return studentIds;
    }

    public void endClass() {
        this.courseClassStatus= CourseClassStatus.Completed;
    }
}
