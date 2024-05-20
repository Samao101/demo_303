package com.example.demo.models.entity;

import com.example.demo.models.entity.base.BaseEntityWithIdLong;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.SheetMarkStatus;
import com.example.demo.validation.ValidSheetMarkStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table
public class SheetMark extends BaseEntityWithIdLong {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseClassId;

    @NotNull
    private Long teacherId;

    @NotNull
    private String courseId;

    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Positive
    @Min(0)
    @Max(10)
    private Double assignmentScore = null; // Điểm bài tập

    @Positive
    @Min(0)
    @Max(10)
    private Double projectScore = null;    // Điểm bài tập lớn

    @Positive
    @Min(0)
    @Max(10)
    private Double midTermScore = null;    // Điểm giữa kỳ

    @Positive
    @Min(0)
    @Max(10)
    private Double finalExamScore = null;  // Điểm cuối kỳ

    private Double finalGrade= null;

    @Enumerated(EnumType.STRING)
    @ValidSheetMarkStatus(anyOf = {SheetMarkStatus.inProgress, SheetMarkStatus.Completed_in_fail, SheetMarkStatus.Completed_in_pass})
    private SheetMarkStatus sheetMarkStatus;

    public SheetMark() {
        this.sheetMarkStatus= SheetMarkStatus.inProgress;
    }

    public void updateFinalGrade() {
        if (assignmentScore == null || projectScore == null || midTermScore == null || finalExamScore == null) {
            // Set status to inProgress if any score is null
            this.sheetMarkStatus = SheetMarkStatus.inProgress;
            return;
        }
        // Calculate final grade if all scores are entered
        this.finalGrade = (assignmentScore * 0.1 + projectScore * 0.2 + midTermScore * 0.2 + finalExamScore * 0.5);
    }

    public void finalizeSheetMark() {
        if (finalGrade == null  || finalGrade < 4.0) {
            sheetMarkStatus = SheetMarkStatus.Completed_in_fail;
        } else {
            sheetMarkStatus = SheetMarkStatus.Completed_in_pass;
        }
    }

    public Long getCourseClassId() {
        updateFinalGrade();
        return courseClassId;
    }

    public void setCourseClassId(Long courseClassId) {
        this.courseClassId = courseClassId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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

    public Double getAssignmentScore() {
        updateFinalGrade();
        return assignmentScore;
    }

    public void setAssignmentScore(Double assignmentScore) {
        updateFinalGrade();
        this.assignmentScore = assignmentScore;
    }

    public Double getProjectScore() {
        updateFinalGrade();
        return projectScore;
    }

    public void setProjectScore(Double projectScore) {
        updateFinalGrade();
        this.projectScore = projectScore;
    }

    public Double getMidTermScore() {
        updateFinalGrade();
        return midTermScore;
    }

    public void setMidTermScore(Double midTermScore) {
        updateFinalGrade();
        this.midTermScore = midTermScore;
    }

    public Double getFinalExamScore() {
        updateFinalGrade();
        return finalExamScore;
    }

    public void setFinalExamScore(Double finalExamScore) {
        updateFinalGrade();
        this.finalExamScore = finalExamScore;
    }

    public Double getFinalGrade() {
        updateFinalGrade();
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        updateFinalGrade();
        this.finalGrade = finalGrade;
    }

    public SheetMarkStatus getSheetMarkStatus() {
        updateFinalGrade();
        return sheetMarkStatus;
    }

    public void setSheetMarkStatus(SheetMarkStatus sheetMarkStatus) {
        updateFinalGrade();
        this.sheetMarkStatus = sheetMarkStatus;
    }

    public void finalSheetMark() {
        updateFinalGrade();
        finalizeSheetMark();
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
