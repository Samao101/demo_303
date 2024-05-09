package com.example.demo.controller;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DTOs.CourseClassDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.DTOs.converter.TeacherDtoConverter;
// import com.example.demo.models.DTOs.request.Qualification.CreateQualificaitonRequest;
import com.example.demo.models.DTOs.request.Teacher.CreateTeacherRequest;
import com.example.demo.models.DTOs.request.Teacher.UpdateTeacherRequest;
// import com.example.demo.models.entity.Qualification;
// import com.example.demo.models.entity.Teacher;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.TeacherService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherDtoConverter teacherDtoConverter;
    private final TeacherService teacherService;
    
    public TeacherController( TeacherDtoConverter teacherDtoConverter,TeacherService teacherService) {
        this.teacherDtoConverter = teacherDtoConverter;
        this.teacherService = teacherService;
    }

// All get request
    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllTeachers() {
        Set<TeacherDto> courses = teacherDtoConverter.convert(teacherService.getAllTeachers());
        if (!courses.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "Found all teachers", courses));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "No teacher available", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getTeacherById(@PathVariable Long id) {
        Optional<TeacherDto> teacher= teacherDtoConverter.convert(teacherService.getTeacherById(id));
        if(teacher.isPresent()) return ResponseEntity.ok(new ResponseObject("success", "Teacher found successfully.", teacher));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Teacher not found with the given ID.", null));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseObject> getTeacherByName(@PathVariable String name) {
        Set<TeacherDto> teachers = teacherDtoConverter.convert(teacherService.getTeacherByName(name));
        if (teachers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "No teachers found with the given name.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Teachers found successfully.", teachers));
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<ResponseObject> getTeachersByCourseId(@PathVariable String courseId) {
        Set<TeacherDto> teachers = teacherDtoConverter.convert(teacherService.getTeachersByCourseId(courseId));
        if (teachers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "No teachers found teaching the course with the given ID.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Teachers teaching the specified course found successfully.", teachers));
    }

    @GetMapping("/{teacherId}/schedule/{dayOfWeek}")
    public ResponseEntity<ResponseObject> getTeachingScheduleByDay(@PathVariable Long teacherId, @PathVariable DayOfWeek dayOfWeek) {
        Set<CourseClassDto> schedule = teacherService.getTeachingScheduleByDay(teacherId, dayOfWeek);
        if (schedule.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("failed", "No teaching schedule found for the teacher on the given day.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Teaching schedule retrieved successfully.", schedule));
    }

    @GetMapping("/{teacherId}/weekly-schedule")
    public ResponseEntity<ResponseObject> getWeeklyTeachingSchedule(@PathVariable Long teacherId) {
        Map<DayOfWeek, Set<CourseClassDto>> weeklySchedule = teacherService.getWeeklyTeachingSchedule(teacherId);
        if (weeklySchedule.isEmpty() || weeklySchedule.values().stream().allMatch(Set::isEmpty)) {
            return ResponseEntity.ok(new ResponseObject("failed", "No teaching schedule found for the teacher for the week.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Weekly teaching schedule retrieved successfully.", weeklySchedule));
    }


    //All Post Request
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createTeacher(@RequestBody CreateTeacherRequest teacherRequest) {
        try {
            TeacherDto teacher = teacherDtoConverter.convert(teacherService.createTeacher(teacherRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Teacher created successfully", teacher ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Error creating teacher:" , null));
        }
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<ResponseObject> createTeachers(@RequestBody Set<CreateTeacherRequest> teacherRequests) {
        Set<String> errors = new HashSet<>();
        Set<TeacherDto> createdTeachers =teacherDtoConverter.convert( teacherService.createTeachers(teacherRequests, errors));

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All teachers created successfully", createdTeachers));
        } else if (createdTeachers.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to create any teachers", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some teachers were created successfully", 
                Map.of("successes", createdTeachers, "errors", errors)));
        }
    }

    // @PostMapping("/{teacherId}/qualification")
    // public ResponseEntity<ResponseObject> addQualificationToTeacher(@RequestBody CreateQualificaitonRequest createQualificaitonRequest) {
    //     Teacher updatedTeacher = teacherService.addQualificationToTeacher(teacherId, qualification);
    //     if (updatedTeacher == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Teacher not found with the given ID, or unable to add qualification.", null));
    //     }
    //     return ResponseEntity.ok(new ResponseObject("success", "Qualification added to teacher successfully.", updatedTeacher));
    // }

    // All put request
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCourse(@PathVariable Long id, @RequestBody UpdateTeacherRequest teacherRequest) {
        try {
            TeacherDto teacherDto = teacherDtoConverter.convert(teacherService.updateTeacher(teacherRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Update successful", teacherDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found teacher with id: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed to update teacher: " + e.getMessage(), null));
        }
    }

    // All delete request
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok(new ResponseObject("success", "Delete teacher successful", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found teacher with id: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed when delete teacher: " + e.getMessage(), null));
        }
    }

    
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllTeacher() {
        teacherService.deleteAllTeacher();
        return ResponseEntity.noContent().build();
    }

}
