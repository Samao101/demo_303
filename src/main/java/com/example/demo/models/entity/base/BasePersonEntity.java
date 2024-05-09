package com.example.demo.models.entity.base;

import java.time.LocalDate;

import com.example.demo.models.entity.enums.Gender;
import com.example.demo.validation.ValidGender;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public abstract class BasePersonEntity extends BaseEntityWithIdLong{

    @NotBlank
    @Column( name = "name" , nullable = false)
    private String name;

    @NotNull
    @Column( name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @ValidGender(anyOf = {Gender.F, Gender.M})
    private Gender gender;

    @NotNull
    @Column
    private LocalDate dateOfBirth;

    @NotBlank
    @Column
    private String placeOfBirth;

    @NotBlank
    @Column( unique = true)
    private String citizenIdentification;

    @NotBlank
    @Column( unique = true)
    private String email;

    @NotBlank
    @Column( unique = true)
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Override
    public String toString() {
        return "BasePersonEntity [name=" + name + ", age=" + age + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth
                + ", placeOfBirth=" + placeOfBirth + ", citizenIdentification=" + citizenIdentification + ", email="
                + email + ", phoneNumber=" + phoneNumber + "]";
    }

    public String getCitizenIdentification() {
        return citizenIdentification;
    }

    public void setCitizenIdentification(String citizenIdentification) {
        this.citizenIdentification = citizenIdentification;
    }

    
}
