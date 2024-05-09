package com.example.demo.models.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.models.entity.base.BasePersonEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Teacher extends BasePersonEntity{

    private String mainCourseId;


    @NotNull
    private Boolean isAvailable;

    private Set<Long> courseClassIds= new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    

    public Teacher() {
        this.courseClassIds =  new HashSet<>();
    }
    
    public void addCourseClass(CourseClass courseClass) {
        this.courseClassIds.add(courseClass.getId());
    }


    public String getMainCourseId() {
        return mainCourseId;
    }

    public void setMainCourseId(String mainCourseId) {
        this.mainCourseId = mainCourseId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Set<Long> getCourseClassIds() {
        return courseClassIds;
    }

    public void setCourseClassIds(Set<Long> courseClassIds) {
        this.courseClassIds = courseClassIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
}
