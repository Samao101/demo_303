package com.example.demo.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DTOs.AllSheetMarkOfCourse;
import com.example.demo.models.DTOs.SheetMarkDto;
import com.example.demo.models.DTOs.converter.SheetMarkDtoConverter;
import com.example.demo.models.DTOs.request.SheetMark.CreateSheetMark;
import com.example.demo.models.DTOs.request.SheetMark.UpdateSheetMark;
import com.example.demo.models.entity.enums.SheetMarkStatus;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.SheetMarkService;

@RestController
@RequestMapping("/sheetmark")
public class SheetMarkController {
    private final SheetMarkDtoConverter sheetMarkDtoConverter;
    private final SheetMarkService sheetMarkService;

    public SheetMarkController( SheetMarkDtoConverter sheetMarkDtoConverter,
                                SheetMarkService sheetMarkService) {
        this.sheetMarkDtoConverter = sheetMarkDtoConverter;
        this.sheetMarkService = sheetMarkService;
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllSheetMarks() {
        Set<SheetMarkDto> sheetMarks = sheetMarkDtoConverter.convert(sheetMarkService.getAllSheetMark());
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved all sheet marks", sheetMarks));
    }

    @GetMapping("/courseClass")
    public ResponseEntity<ResponseObject> getSheetMarksByCourseClassId(@RequestParam Long courseClassId) {
        Set<SheetMarkDto> sheetMarks = sheetMarkDtoConverter.convert(sheetMarkService.getSheetMarkByCourseClassId(courseClassId));
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved sheet marks for course class", sheetMarks));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponseObject> getSheetMarksByStudentId(@PathVariable Long studentId) {
        Set<SheetMarkDto> sheetMarks = sheetMarkDtoConverter.convert(sheetMarkService.getSheetMarkByStudentId(studentId));
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved sheet marks for student", sheetMarks));
    }

    @GetMapping("/student_status")
    public ResponseEntity<ResponseObject> getSheetMarksByStudentIdAndStatus( @RequestParam Long studentId, @RequestParam SheetMarkStatus status) {
        Set<SheetMarkDto> sheetMarks = sheetMarkDtoConverter.convert(sheetMarkService.getSheetMarkByStudentIdAndStatus(studentId, status));
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved sheet marks for student with status", sheetMarks));
    }

    @GetMapping("/student_courseclass")
    public ResponseEntity<ResponseObject> getSheetMarksByStudentIdAndCourseClassId( @RequestParam Long studentId, @RequestParam Long courseClassId) {
        Set<SheetMarkDto> sheetMarks = sheetMarkDtoConverter.convert(sheetMarkService.getSheetMarkByStudentIdAndCourseClassId(studentId, courseClassId));
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved sheet marks for student with status", sheetMarks));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponseObject> getSheetMarksByTeacherId(@PathVariable Long teacherId) {
        Set<SheetMarkDto> sheetMarks = sheetMarkDtoConverter.convert(sheetMarkService.getSheetMarkByTeacherId(teacherId));
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved sheet marks for teacher", sheetMarks));
    }

    @GetMapping("/course")
    public ResponseEntity<ResponseObject> getSheetMarksForOneCourseForOneStudent(@RequestParam Long studentId, @RequestParam String courseId) {
        AllSheetMarkOfCourse allSheetMarkOfCourse = sheetMarkService.getAllSheetMarkOfCourseByStudentId(courseId, studentId);
        return ResponseEntity.ok(new ResponseObject("success", "Retrieved sheet marks for student in course", allSheetMarkOfCourse));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createSheetMark(@RequestBody CreateSheetMark createRequest) {
        try {
            SheetMarkDto newSheetMark = sheetMarkDtoConverter.convert(sheetMarkService.createSheetMark(createRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Sheet mark created successfully", newSheetMark));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // @PutMapping
    // public ResponseEntity<ResponseObject> updateSheetMark(@RequestBody UpdateSheetMark updateRequest) {
    //     try {
    //         SheetMarkDto updatedSheetMark = sheetMarkDtoConverter.convert(sheetMarkService.updateSheetMark(updateRequest));
    //         return ResponseEntity.ok(new ResponseObject("success", "Sheet mark updated successfully", updatedSheetMark));
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
    //     }
    // }


    @PutMapping("/assignScores")
    public ResponseEntity<ResponseObject> assignScores(@RequestParam Long studentId,
                                                       @RequestParam Long courseClassId,
                                                       @RequestParam(required = false) Double assignmentScore,
                                                       @RequestParam(required = false) Double projectScore,
                                                       @RequestParam(required = false) Double midTermScore,
                                                       @RequestParam(required = false) Double finalExamScore) {
        try {
            sheetMarkService.assignScores(studentId, courseClassId, assignmentScore, projectScore, midTermScore, finalExamScore);
            return ResponseEntity.ok(new ResponseObject("success", "Scores assigned successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @PutMapping("/assignManyScores")
    public ResponseEntity<ResponseObject> assignManyScores(@RequestBody Set<UpdateSheetMark> updateSheetMarks) {
        try {
            sheetMarkService.assignManyScores(updateSheetMarks);
            return ResponseEntity.ok(new ResponseObject("success", "Scores assigned successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }


    @DeleteMapping("/{sheetMarkId}")
    public ResponseEntity<ResponseObject> deleteSheetMark(@PathVariable Long sheetMarkId) {
        try {
            sheetMarkService.deleteCourse(sheetMarkId);
            return ResponseEntity.ok(new ResponseObject("success", "Sheet mark deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseObject> deleteAllSheetMarks() {
        sheetMarkService.deleteAllSheetMark();
        return ResponseEntity.ok(new ResponseObject("success", "All sheet marks deleted successfully", null));
    }
}
