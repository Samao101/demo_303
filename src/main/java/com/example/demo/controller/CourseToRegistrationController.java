package com.example.demo.controller;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.example.demo.models.DTOs.CourseToRegistrationDto;
import com.example.demo.models.DTOs.converter.CourseToRegistrationDtoConverter;
import com.example.demo.models.DTOs.request.CourseToRegistration.CreateCourseToRegistration;
import com.example.demo.models.DTOs.request.CourseToRegistration.UpdateCourseToRegistration;
import com.example.demo.models.entity.CourseRecommendation;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.CourseClassService;
import com.example.demo.service.CourseToRegistrationService;

@RestController
@RequestMapping("/coursetoregistration")
public class CourseToRegistrationController {
    private final CourseToRegistrationDtoConverter courseToRegistrationDtoConverter;

    private final CourseToRegistrationService courseToRegistrationService;
    private final CourseClassService courseClassService;

    public CourseToRegistrationController(  CourseToRegistrationDtoConverter courseToRegistrationDtoConverter,
                                            CourseToRegistrationService courseToRegistrationService,
                                            CourseClassService courseClassService) {
        this.courseToRegistrationDtoConverter = courseToRegistrationDtoConverter;

        this.courseToRegistrationService = courseToRegistrationService;
        this.courseClassService = courseClassService;
    }


    // All get request
    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getAllRegistrations() {
        Set<CourseToRegistrationDto> registrations = courseToRegistrationDtoConverter.convert(courseToRegistrationService.getAll());
        if(registrations.size() == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not_found", "No registration available", null));
        else return ResponseEntity.ok(new ResponseObject("success", "All registrations fetched successfully", registrations));
    }

    @GetMapping("/registrationId/{id}")
    public ResponseEntity<ResponseObject> getRegistrationById(@PathVariable Long id) {
        Optional<CourseToRegistrationDto> registration = courseToRegistrationDtoConverter.convert(courseToRegistrationService.getById(id));
        if(!registration.isPresent()) return ResponseEntity.badRequest().body(new ResponseObject("error", "No registration found with id " + id, null));
        else return ResponseEntity.ok(new ResponseObject("success", "Registration fetched successfully", registration.get()));
    }

    @GetMapping("/by-course")
    public ResponseEntity<ResponseObject> getRegistrationsByCourseId(@RequestParam String courseId) {
        Set<CourseToRegistrationDto> registrations = courseToRegistrationDtoConverter.convert(courseToRegistrationService.getByCourseId(courseId));
        return ResponseEntity.ok(new ResponseObject("success", "Registrations fetched successfully", registrations));
    }

    @GetMapping("/by-student-id")
    public ResponseEntity<ResponseObject> getRegistrationsByStudentId(@RequestParam Long studentId) {
        try {
            Set<CourseToRegistrationDto> registrations = courseToRegistrationDtoConverter.convert(
                courseToRegistrationService.getAllRegistrationOfStudentId(studentId)
            );
            if (registrations.isEmpty()) {
                return ResponseEntity.ok(new ResponseObject("success", "No registrations found for the student", null));
            }
            return ResponseEntity.ok(new ResponseObject("success", "Registrations fetched successfully", registrations));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // Kiểm tra xem đã tạo phiếu đăng ký chưa
    @GetMapping("/check")
    public ResponseEntity<ResponseObject> check() {
        if (courseToRegistrationService.checkRegistration()) {
            return ResponseEntity.ok(new ResponseObject("success", "Registration is available", null));
        } else {
            return ResponseEntity.ok(new ResponseObject("success", "Registration is not available", null));
        }
    }

    //Dungf de lay phieu dang ky dua tren course id vaf course level
    @GetMapping("/by-course-and-level")
    public ResponseEntity<ResponseObject> findByCourseIdAndLevel(
        @RequestParam String courseId, 
        @RequestParam CourseLevel courseLevel) {
        Set<CourseToRegistrationDto> registrations = courseToRegistrationDtoConverter.convert(courseToRegistrationService.getByCourseIdAndLevel(courseId, courseLevel));
        if(registrations.size()!= 0)return ResponseEntity.ok(new ResponseObject("success", "Registrations for specified course ID and level fetched successfully", registrations));
        else return ResponseEntity.badRequest().body(new ResponseObject("Not_found", "Not found any registrations match the condition" ,null));
    }

    
    @GetMapping("/get")
    public ResponseEntity<ResponseObject> getRegistrations(@RequestParam String courseId, @RequestParam CourseLevel courseLevel, @RequestParam DayOfWeek dayOfWeek) {
        Set<CourseToRegistrationDto> registrations = courseToRegistrationDtoConverter.convert(courseToRegistrationService.getCourseToRegistrations(courseId, courseLevel, dayOfWeek));
        if(registrations.size()!= 0) return ResponseEntity.ok(new ResponseObject("success", "Registrations fetched successfully", registrations));
        else return ResponseEntity.badRequest().body(new ResponseObject("Not_found", "Not found any registrations match the condition" ,null));
    }

    // Dung de lay cac phieu dang ky phu hop voi tung sinh vien
    @GetMapping("/for-student-id")
    public ResponseEntity<ResponseObject> getRegistrationsForStudentId(@RequestParam Long studentId) {
        try {
            Set<CourseRecommendation> registrations = courseToRegistrationService.getSuitableRegistForStudent(studentId);
            if (registrations.isEmpty()) {
                return ResponseEntity.ok(new ResponseObject("success", "No suitable found for the student", null));
            }
            return ResponseEntity.ok(new ResponseObject("success", "Registrations fetched successfully", registrations));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    // All Post Request
    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCourseToRegistration(@RequestBody CreateCourseToRegistration courseDto) {
        try {
            CourseToRegistrationDto course = courseToRegistrationDtoConverter.convert(courseToRegistrationService.createCourse(courseDto));
            return ResponseEntity.ok(new ResponseObject("success", "Course registration created successfully", course));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<ResponseObject> createMultipleCourseRegistrations(@RequestBody Set<CreateCourseToRegistration> courseRequests) {
        Set<String> errors = new HashSet<>();
        Set<CourseToRegistrationDto> createdCourses = courseToRegistrationDtoConverter.convert(courseToRegistrationService.createMultipleCourses(courseRequests, errors));

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All courses registered successfully", createdCourses));
        } else if (createdCourses.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to register any courses", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some courses registered successfully but some failed", 
                Map.of("registeredCourses", createdCourses, "errors", errors)));
        }
    }


// All Put Requeset
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCourseRegistration( @RequestBody UpdateCourseToRegistration courseDto) {
        try {
            CourseToRegistrationDto updatedCourse = courseToRegistrationDtoConverter.convert(courseToRegistrationService.updateCourse(courseDto));
            return ResponseEntity.ok(new ResponseObject("success", "Course registration updated successfully", updatedCourse));
        } catch (RuntimeException  e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<ResponseObject> updateCourseRegistrations(@RequestBody Set<UpdateCourseToRegistration> updateCourseToRegistrations) {
        Set<String> errors = new HashSet<>();
        Set<CourseToRegistrationDto> updatedCourseToRegistrationDtos = courseToRegistrationDtoConverter.convert(courseToRegistrationService.updateCourses(updateCourseToRegistrations, errors));

        if (errors.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("success", "All course to registration updated successfully", updatedCourseToRegistrationDtos));
        } else if (updatedCourseToRegistrationDtos.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Failed to update any courses to registration", errors));
        } else {
            return ResponseEntity.ok(new ResponseObject("partial_success", "Some courses to registration were updated successfully",
                                                       Map.of("successes", updatedCourseToRegistrationDtos, "errors", errors)));
        }
    }

    // ALl delete requeset
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        return courseToRegistrationService.getById(id)
                .map(registration -> {
                    courseToRegistrationService.deleteById(id);
                    return ResponseEntity.ok(new ResponseObject("success", "Course registration deleted successfully", null));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Course registration not found with id: " + id, null)));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ResponseObject> deleteAll() {
        courseToRegistrationService.deleteAllCourseToRegistration();
        return ResponseEntity.ok(new ResponseObject("success", "All registrations deleted successfully", null));
    }
      

// Start new semester
    // 1: Mở phiếu đăng ký ở Course Controller

    // 2: Check xem có đủ giáo viên cho tất cả các lớp không ở Course to registration
    @GetMapping("/check-teachers")
    public ResponseEntity<ResponseObject> checkForTeacher() {
        try {
            Set<String> insufficientCourses = courseToRegistrationService.checkForTeacher();
            if (insufficientCourses.isEmpty()) {
                return ResponseEntity.ok(new ResponseObject("success", "All courses have sufficient teachers.", null));
            } else {
                return ResponseEntity.ok(new ResponseObject("warning", "Some courses have insufficient teachers.", insufficientCourses));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseObject("error", "Error checking teacher availability: " + e.getMessage(), null));
        }
    }
    
    // 3: Mở lớp cho tất cả học sinh dựa trên phiếu đăng ký ở Course to registration
    @GetMapping("/process-all")
    public ResponseEntity<ResponseObject> processAllRegistrations() {
        if(courseClassService.checkClassInProgress()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ResponseObject("Fail", "Please ensure all class end", null));
        }
        try {
            Set<String> insufficientCourses = courseToRegistrationService.checkForTeacher();
            if(insufficientCourses.size() != 0) {
                String courseIsMissing ="";
                for(String s : insufficientCourses) courseIsMissing += (s +" ");
                return ResponseEntity.badRequest().body(new ResponseObject("error", "Not enough teachers available for some course such as: " + courseIsMissing, null));
            }
            courseToRegistrationService.endRegistration();
            return ResponseEntity.ok(new ResponseObject("success", "All course registrations processed successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseObject("error", "Error processing all registrations: " + e.getMessage(), null));
        }
    }

    @GetMapping("/process-all-without-teacher")
    public ResponseEntity<ResponseObject> processAllRegistrationsWithoutTeacher() {
        if(courseClassService.checkClassInProgress()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ResponseObject("Fail", "Please ensure all class end", null));
        }
        try {
            courseToRegistrationService.endRegistration();
            return ResponseEntity.ok(new ResponseObject("success", "All course registrations processed successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseObject("error", "Error processing all registrations: " + e.getMessage(), null));
        }
    }
    
    // 4: Chính thức tất cả các lớp inProgres ở CouresClassController
}
