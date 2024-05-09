package com.example.demo.controller;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.DTOs.CourseClassDto;
import com.example.demo.models.DTOs.SheetMarkDto;
import com.example.demo.models.DTOs.StudentCreditsAndGPADTO;
import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.DTOs.converter.SheetMarkDtoConverter;
import com.example.demo.models.DTOs.converter.StudentDtoConverter;
import com.example.demo.models.DTOs.request.Student.CreateStudentRequest;
import com.example.demo.models.DTOs.request.Student.UpdateStudentRequest;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.StudentService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentDtoConverter studentDtoConverter;
    private final SheetMarkDtoConverter sheetMarkDtoConverter;

    private final StudentService studentService;

    public StudentController(   StudentDtoConverter studentDtoConverter ,
                                StudentService studentService,
                                SheetMarkDtoConverter sheetMarkDtoConverter) {
        this.studentDtoConverter = studentDtoConverter;
        this.sheetMarkDtoConverter = sheetMarkDtoConverter;
        this.studentService = studentService;
    }
    

// All get request
    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllStudents() {
        Set<StudentDto> students = studentDtoConverter.convert(studentService.getAllStudents());
        if (!students.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("ok", "Fetch all students successfully", students));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not_found", "No student available", null));
        }
    }

    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<ResponseObject> getStudentById(@PathVariable Long studentId) {
        Optional<StudentDto> student = studentDtoConverter.convert(studentService.getStudentById(studentId));
        if(student.isPresent())return ResponseEntity.ok(new ResponseObject("ok", "Fetch student successfully", student.get()));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Cannot find student with id=" + studentId, null));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseObject> getStudentByName(@PathVariable String name) {
        Set<StudentDto> studentDtos = studentDtoConverter.convert(studentService.getStudentByName(name));
        if (studentDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "No student found with the given name.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Students found successfully.", studentDtos));
    }

    // Lấy lịch theo ngày
    @GetMapping("/{studentId}/schedule/{dayOfWeek}")
    public ResponseEntity<ResponseObject> getStudyingScheduleByDay(@PathVariable Long studentId, @PathVariable DayOfWeek dayOfWeek) {
        Set<CourseClassDto> schedule = studentService.getStudyingSchedule(studentId, dayOfWeek);
        if (schedule.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("failed", "No studying schedule found for the student on the given day.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Studying schedule retrieved successfully.", schedule));
    }

    // Lấy lịch theo tuần
    @GetMapping("/{studentId}/weekly-schedule")
    public ResponseEntity<ResponseObject> getWeeklyTeachingSchedule(@PathVariable Long studentId) {
        Map<DayOfWeek, Set<CourseClassDto>> weeklySchedule = studentService.getWeeklyStudyingSchedule(studentId);
        if (weeklySchedule.isEmpty() || weeklySchedule.values().stream().allMatch(Set::isEmpty)) {
            return ResponseEntity.ok(new ResponseObject("failed", "No studying schedule found for the student for the week.", null));
        }
        return ResponseEntity.ok(new ResponseObject("success", "Weekly studying schedule retrieved successfully.", weeklySchedule));
    }

    // Lấy điểm tổng tất cả các môn đã học
    @GetMapping("/{studentId}/credits-gpa")
    public ResponseEntity<ResponseObject> getCreditsAndGPA(@PathVariable Long studentId) {
        try {
            StudentCreditsAndGPADTO result = studentService.calculateCreditsAndGPA(studentId);
            return ResponseEntity.ok(new ResponseObject("success", "Successfully calculated GPA and total credits", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // Lấy điểm của các môn đang học
    @GetMapping("/{studentId}/currentMark")
    public ResponseEntity<ResponseObject> getCurrentMark(@PathVariable Long studentId) {
        try {
            Set<SheetMarkDto> result = sheetMarkDtoConverter.convert(studentService.getCurrentMark(studentId));
            return ResponseEntity.ok(new ResponseObject("success", "Successfully with all current sheet mark", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

// All post request
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createStudent(@RequestBody CreateStudentRequest studentRequest) {
        try {
            StudentDto student = studentDtoConverter.convert(studentService.createStudent(studentRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Student created successfully", student ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Error creating student:" + e.getMessage(), null));
        }
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<ResponseObject> createStudents(@RequestBody Set<CreateStudentRequest> studentRequests) {
        Set<String> errors = new HashSet<>();
        Set<StudentDto> createdStudents =studentDtoConverter.convert( studentService.createStudents(studentRequests, errors));

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All students created successfully", createdStudents));
        } else if (createdStudents.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to create any students", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some students were created successfully", 
                Map.of("successes", createdStudents, "errors", errors)));
        }
    }


    // Đăng ký vào phiếu đăng ký
    @PostMapping("/{registrationId}/registration/{studentId}")
    public ResponseEntity<ResponseObject> registerStudent(@PathVariable Long registrationId, @PathVariable Long studentId) {
        try {
            String result = studentService.registerStudentToCourse(registrationId, studentId);
            if (result.equals("Student registered successfully")) {
                return ResponseEntity.ok(new ResponseObject("success", result, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseObject("fail", result, null));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // Hủy đăng ký ở phiếu đăng ký
    @PostMapping("/{registrationId}/cancel/{studentId}")
    public ResponseEntity<ResponseObject> cancelRegistration(@PathVariable Long registrationId, @PathVariable Long studentId) {
        try {
            String message = studentService.cancelRegisterStudentToCourse(registrationId, studentId);
            return ResponseEntity.ok(new ResponseObject("success", message, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // Register student to a class
    @PostMapping("/{classId}/students/{studentId}/register")
    public ResponseEntity<ResponseObject> registerStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            String message = studentService.registerStudentToClass(classId, studentId);
            return ResponseEntity.ok(new ResponseObject("success", message, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // Cancel registration of a student from a class
    @PostMapping("/{classId}/students/{studentId}/cancel")
    public ResponseEntity<ResponseObject> cancelClass(@PathVariable Long classId, @PathVariable Long studentId) {
        try {
            String message = studentService.cancekRegisterStudentToClass(classId, studentId);
            return ResponseEntity.ok(new ResponseObject("success", message, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", e.getMessage(), null));
        }
    }



// All put request
    @PutMapping
    public ResponseEntity<ResponseObject> updateStudent(@RequestBody UpdateStudentRequest studentRequest) {
        try {
            StudentDto student = studentDtoConverter.convert(studentService.updateStudent(studentRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Update successful", student));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found course with id: " + studentRequest.getId(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed to update student: " + e.getMessage(), null));
        }
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<ResponseObject> updateStudents(@RequestBody Set<UpdateStudentRequest> studentRequests) {
        Set<String> errors = new HashSet<>();
        Set<StudentDto> updatedStudents = studentDtoConverter.convert(studentService.updateStudents(studentRequests, errors));

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All students updated successfully", updatedStudents));
        } else if (updatedStudents.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to update any students", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some students were updated successfully",
                                                       Map.of("successes", updatedStudents, "errors", errors)));
        }
    }

// All delete request
    @DeleteMapping("/studentId/{id}")
    public ResponseEntity<ResponseObject> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok(new ResponseObject("success", "Delete student successful", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found student with id: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed when delete student: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllStudent() {
        studentService.deleteAllStudent();
        return ResponseEntity.noContent().build();
    }
}
