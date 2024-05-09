package com.example.demo.models.entity;

import com.example.demo.models.entity.enums.CourseLevel;

public class CourseRecommendation {
    private String courseId;
    private CourseLevel courseLevel;

    public CourseRecommendation(String courseId, CourseLevel courseLevel) {
        this.courseId = courseId;
        this.courseLevel = courseLevel;
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
}
