package com.example.demo.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.Repositories.QualificationRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.request.Qualification.CreateQualificaitonRequest;
import com.example.demo.models.DTOs.request.Qualification.UpdateQualificationRequest;
import com.example.demo.models.entity.Qualification;
import com.example.demo.models.entity.Teacher;

@Service
public class QualificationService {
    private final QualificationRepository qualificationRepository;
    private final TeacherRepository teacherRepository;

    public QualificationService(QualificationRepository qualificationRepository, TeacherRepository teacherRepository) {
        this.qualificationRepository = qualificationRepository;
        this.teacherRepository = teacherRepository;
    }

// All method for get request
    public Set<Qualification> findAllQualificationsByTeacherId(Long teacherId) {
        return qualificationRepository.findByTeacherId(teacherId);
    }



// All method for post request
    public Qualification createQualification(CreateQualificaitonRequest createQualificaitonRequest) {
        Teacher teacher = teacherRepository.findById(createQualificaitonRequest.getTeacherId())
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id "+ createQualificaitonRequest.getTeacherId()));
        
        Qualification qualification = new Qualification();

        qualification.setInstitution(createQualificaitonRequest.getInstitution());
        qualification.setName(createQualificaitonRequest.getName());
        qualification.setTeacher(teacher);
        qualification.setYear(createQualificaitonRequest.getYear());
        
        return qualificationRepository.save(qualification);
    }

// All method for put request
    public Qualification updateQualification(UpdateQualificationRequest updateQualificationRequest) {
        Qualification qualification = qualificationRepository.findById(updateQualificationRequest.getId())
            .orElseThrow(() -> new IllegalArgumentException("Qualification not found with id "+ updateQualificationRequest.getId()));
        
        Teacher teacher = teacherRepository.findById(updateQualificationRequest.getTeacherId())
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: "+ updateQualificationRequest.getTeacherId()));
        if(teacher.getId() != qualification.getTeacher().getId()) {
            throw new IllegalArgumentException("Teacher id can not be change of this qualification!!!");
        }
        
        qualification.setInstitution(updateQualificationRequest.getInstitution());
        qualification.setName(updateQualificationRequest.getName());
        qualification.setYear(updateQualificationRequest.getYear());
        
        return qualificationRepository.save(qualification);
    }

// ALl method for delete request
    public void deleteQualification(Long qualificationId) {
        Qualification qualification = qualificationRepository.findById(qualificationId)
                .orElseThrow(() -> new IllegalArgumentException("Qualification not found with id: "+ qualificationId));

        qualificationRepository.delete(qualification);
    }

    public void deleteQualificationsByTeacherId(Long teacherId) {
        Set<Qualification> qualifications = qualificationRepository.findByTeacherId(teacherId);
        qualificationRepository.deleteAll(qualifications);
    }
}
