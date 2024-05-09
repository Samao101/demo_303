package com.example.demo.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DTOs.request.Qualification.CreateQualificaitonRequest;
import com.example.demo.models.entity.Qualification;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.QualificationService;

@RestController
@RequestMapping("/qualification")
public class QualificationController {
    private final QualificationService qualificationService;

    public QualificationController( QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllQualifications(@PathVariable Long teacherId) {
        Set<Qualification> qualifications = qualificationService.findAllQualificationsByTeacherId(teacherId);
        if (qualifications.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("failed", "No qualifications found for the teacher with ID: " + teacherId, null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Qualifications retrieved successfully.", qualifications));
    }

    @PostMapping("/qualification")
    public ResponseEntity<ResponseObject> createQualification(@RequestBody CreateQualificaitonRequest createQualificaitonRequest) {
        try {
            qualificationService.createQualification(createQualificaitonRequest);
            return ResponseEntity.ok(new ResponseObject("success", "Qualification created successfully.", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Teacher not found with the given ID, or unable to add qualification.", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseObject> deleteAllQualifications(@PathVariable Long teacherId) {
        try {
            qualificationService.deleteQualificationsByTeacherId(teacherId);
            return ResponseEntity.ok(new ResponseObject("success", "All qualifications for the teacher with ID: " + teacherId + " have been deleted.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Error occurred while deleting qualifications: " + e.getMessage(), null));
        }
    }
}
