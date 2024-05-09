package com.example.demo.validation;

import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsedEmailValidator implements ConstraintValidator<UsedEmail, String> {

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    public UsedEmailValidator(StudentRepository studentRepository,
                              TeacherRepository teacherRepository
                              ) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return studentRepository.findByEmail(email).isPresent() ||
                teacherRepository.findByEmail(email).isPresent();
    }
}
