package com.example.demo.models.DTOs;

public class StudentCreditsAndGPADTO {
    private Double totalCredits;
    private Double gpaOnScaleTen;
    private Double gpaOnScaleFour;

    // Constructors, getters, and setters
    public StudentCreditsAndGPADTO(Double totalCredits, Double gpaOnScaleTen, Double gpaOnScaleFour) {
        this.totalCredits = totalCredits;
        this.gpaOnScaleTen = gpaOnScaleTen;
        this.gpaOnScaleFour = gpaOnScaleFour;
    }

    public Double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Double getGpaOnScaleTen() {
        return gpaOnScaleTen;
    }

    public void setGpaOnScaleTen(Double gpaOnScaleTen) {
        this.gpaOnScaleTen = gpaOnScaleTen;
    }

    public Double getGpaOnScaleFour() {
        return gpaOnScaleFour;
    }

    public void setGpaOnScaleFour(Double gpaOnScaleFour) {
        this.gpaOnScaleFour = gpaOnScaleFour;
    }
}
