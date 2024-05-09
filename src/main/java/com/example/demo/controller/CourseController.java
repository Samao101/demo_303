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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DTOs.CourseDto;
import com.example.demo.models.DTOs.converter.CourseDtoConverter;
import com.example.demo.models.DTOs.request.Course.BaseCourseRequest;
import com.example.demo.models.entity.Course;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseDtoConverter courseDtoConverter;

    private final CourseService courseService;

    public CourseController(CourseDtoConverter courseDtoConverter,CourseService courseService) {
        this.courseDtoConverter = courseDtoConverter;
        this.courseService = courseService;
    }

    // ALl Get Method

    // Lấy tất cả các khóa học đã tạo
    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllCourses() {
        Set<CourseDto> courses = courseDtoConverter.convert(courseService.getAllCourses());
        if (!courses.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "Found all courses", courses));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not_found", "No course available", null));
        }
    }

    // Lấy tất cả các khóa học trường đang phục vụ
    @GetMapping("/getAvailable")
    public ResponseEntity<ResponseObject> getAvailableCourses() {
        Set<CourseDto> courses = courseDtoConverter.convert(courseService.getCourseByIsAvailable(true));
        if (!courses.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "Found courses is available", courses));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not_found", "No course available", null));
        }
    }

    // Lấy khóa học theo id xác định
    @GetMapping("/courseId/{courseId}")
    public ResponseEntity<ResponseObject> getCourseByCourseId(@PathVariable String courseId) {
        Optional<CourseDto> courseOptional= courseDtoConverter.convert(courseService.getCourseByCourseId(courseId));
        if(courseOptional.isPresent()) {
            return ResponseEntity.ok(new ResponseObject("success", "Found course with id : " + courseId, courseOptional.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Can't found course with id " + courseId, null));
    }


    // Lấy khóa học theo tên khóa học xác định
    @GetMapping("/name/{courseName}")
    public ResponseEntity<ResponseObject> getCourseByCourseName(@PathVariable String courseName) {
        Optional<CourseDto> courseOptional= courseDtoConverter.convert(courseService.getCoursesByCourseName(courseName));
        if(courseOptional.isPresent()) {
            return ResponseEntity.ok(new ResponseObject("success", "Found course with id : " + courseName, courseOptional.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Can't found course with id " + courseName, null));
    }

    //Lấy khóa học theo ngày diễn ra
    @GetMapping("/day")
    public ResponseEntity<ResponseObject> getCoursesByDayOfWeek(@RequestParam DayOfWeek dayOfWeek) {
        Set<CourseDto> courses = courseDtoConverter.convert(courseService.getCoursesByDayOfWeek(dayOfWeek));
        if (!courses.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All courses take place on: " + dayOfWeek, courses));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "No course take place on " + dayOfWeek, null));
        }
    }

 //All Post Method
    // Request tạo đúng 1 khóa học 
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCourse(@RequestBody BaseCourseRequest courseDto) {
        try {
            Course course = courseService.createCourse(courseDto);
            return ResponseEntity.ok(new ResponseObject("success", "Course created successfully", course));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // Request tạo nhieuf khóa học
    @PostMapping("/bulk-create")
    public ResponseEntity<ResponseObject> createCourses(@RequestBody Set<BaseCourseRequest> courseDtos) {
        Set<String> successes = new HashSet<>();
        Set<String> errors = new HashSet<>();

        courseService.createCourses(courseDtos, successes, errors);

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All courses created successfully.", successes));
        } else if (successes.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "No courses could be created.", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some courses were created successfully.", 
                                                       Map.of("successes", successes, "errors", errors)));
        }
    }




//ALL Put Request
    // Cập nhập thông tin lại của một khóa học xác định
    @PutMapping
    public ResponseEntity<ResponseObject> updateCourse(@RequestBody BaseCourseRequest courseDetails) {
        try {
            Course updatedCourse = courseService.updateCourse(courseDetails);
            return ResponseEntity.ok(new ResponseObject("success", "Update successful", courseDtoConverter.convert(updatedCourse)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed to update data: " + e.getMessage(), null));
        }
    }

    // Cập nhập thông tin lại của nhiều khóa học 
    @PutMapping("/bulk-update")
    public ResponseEntity<ResponseObject> updateCourses(@RequestBody Set<BaseCourseRequest> courseUpdates) {
        Set<String> successes = new HashSet<>();
        Set<String> errors = new HashSet<>();

        courseService.updateCourses(courseUpdates, successes, errors);

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All courses updated successfully.", successes));
        } else if (successes.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to update any courses.", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some courses were updated successfully.",
                                                       Map.of("successes", successes, "errors", errors)));
        }
    }

    @PutMapping("/endByCourseId/{courseId}")
    public ResponseEntity<ResponseObject> endCourse(@PathVariable String courseID) {
        try {
            courseService.endCourse(courseID);
            return ResponseEntity.ok(new ResponseObject("success", "End course successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed to end course: " + e.getMessage(), null));
        }
    }


//All Delete Request
    // Xóa một khóa học nào đó
    @DeleteMapping("/courseId/{courseId}")
    public ResponseEntity<ResponseObject> deleteCourse(@PathVariable String courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok(new ResponseObject("success", "Delete course successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed when delete course: " + e.getMessage(), null));
        }
    }

    // Xóa tất cả khóa học
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllCourses() {
        courseService.deleteAllCourses();
        return ResponseEntity.noContent().build();
    }

//Start new semester    
    // 1: Mở đăng ký cho các khóa học ở Course controller
    @PostMapping("/startForRegist")
    public ResponseEntity<ResponseObject> createRegistrationsForAllCourses() {
        
        try {
            courseService.createRegistrationsForAllCourses();
            return ResponseEntity.ok(new ResponseObject("success", "All registrations created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to create registrations: " + e.getMessage(), null));
        }
    }
    // 2: Check xem có đủ giáo viên cho tất cả các lớp không ở CourseToRegistrationController
    // 3: Mở lớp cho tất cả học sinh dựa trên phiếu đăng ký ở CourseToRegistrationController
    // 4: Chính thức tất cả các lớp inProgres ở CouresClassController
}
