package com.example.demo.controller;

import java.time.DayOfWeek;
import java.util.Set;
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

import com.example.demo.models.DTOs.CourseClassDto;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.DTOs.converter.CourseClassDtoConverter;
import com.example.demo.models.DTOs.converter.TeacherDtoConverter;
import com.example.demo.models.DTOs.request.CourseClass.CreateCourseClass;
import com.example.demo.models.DTOs.request.CourseClass.UpdateCourseClass;
import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.CourseClassService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/courseclass")
public class CourseClassController {
    private final CourseClassDtoConverter courseClassDtoConverter;
    private final TeacherDtoConverter teacherDtoConverter;

    private final CourseClassService courseClassService;

    public CourseClassController(CourseClassDtoConverter courseClassDtoConverter ,
                                CourseClassService courseClassService,
                                TeacherDtoConverter teacherDtoConverter) {
        this.courseClassDtoConverter = courseClassDtoConverter;
        
        this.courseClassService = courseClassService;
        this.teacherDtoConverter  = teacherDtoConverter;
    }


    // All get request
    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllCourseClasses() {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getAllCourseClasses());
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class", null));
        return ResponseEntity.ok(new ResponseObject("success", "All course classes fetched successfully", dtos));
    }

    @GetMapping("/all_in_progress")
    public ResponseEntity<ResponseObject> getAllCourseClassesInProgress() {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClassByStatus(CourseClassStatus.inProgress));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class", null));
        return ResponseEntity.ok(new ResponseObject("success", "All course classes fetched successfully", dtos));
    }

    @GetMapping("/all_in_plan")
    public ResponseEntity<ResponseObject> getAllCourseClassesInPlan() {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClassByStatus(CourseClassStatus.inPlanned));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class", null));
        return ResponseEntity.ok(new ResponseObject("success", "All course classes fetched successfully", dtos));
    }

    @GetMapping("/get_by_status")
    public ResponseEntity<ResponseObject> getAllCourseClassesByStatus(@RequestParam CourseClassStatus courseClassStatus) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClassByStatus(courseClassStatus));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class", null));
        return ResponseEntity.ok(new ResponseObject("success", "All course classes fetched successfully", dtos));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getClassById(@PathVariable Long id) {
        Optional<CourseClassDto> dto = courseClassDtoConverter.convert(courseClassService.getClassById(id));
        if(dto.isPresent()) return ResponseEntity.ok(new ResponseObject("success", "Course class fetched successfully", dto.get()));
        else return ResponseEntity.status(404).body(new ResponseObject("error", "Course class not found", null));
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<ResponseObject> getClassByCourseId(@PathVariable String courseId) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClassByCourseId(courseId));
        if(dtos.size()==0 ) return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this course id: "+ courseId, null));
        else return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }

    @GetMapping("/studentId_inprogress/{studentId}")
    public ResponseEntity<ResponseObject> getClassByStudentIdAndInprogess(@PathVariable Long studentId) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getCurrentClassOfStudent(studentId, CourseClassStatus.inProgress));
        if(dtos.size()==0 ) return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this student id: "+ studentId, null));
        else return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }

    @GetMapping("/studentId_inplan/{studentId}")
    public ResponseEntity<ResponseObject> getClassByStudentIdAndInplan(@PathVariable Long studentId) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getCurrentClassOfStudent(studentId, CourseClassStatus.inPlanned));
        if(dtos.size()==0 ) return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this student id: "+ studentId, null));
        else return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }

    @GetMapping("/studentId_complete/{studentId}")
    public ResponseEntity<ResponseObject> getClassByStudentIdAndComplete(@PathVariable Long studentId) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getCurrentClassOfStudent(studentId, CourseClassStatus.Completed));
        if(dtos.size()==0 ) return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this student id: "+ studentId, null));
        else return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }

    @GetMapping("/by-course-and-level")
    public ResponseEntity<ResponseObject> getClassByCourseIdAndLevel(@RequestParam String courseId, @RequestParam CourseLevel courseLevel) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClassByCourseIdAndLevel(courseId, courseLevel));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this condition", null));
        return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }

    @GetMapping("/by-day")
    public ResponseEntity<ResponseObject> getCoursesByDayOfWeek(@RequestParam DayOfWeek dayOfWeek) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getCoursesByDayOfWeek(dayOfWeek));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this condition", null));
        return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> getClasses(@RequestParam(required = false) String courseId, @RequestParam(required = false) CourseLevel courseLevel, @RequestParam(required = false) DayOfWeek dayOfWeek) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClasses(courseId, courseLevel, dayOfWeek));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this condition", null));
        return ResponseEntity.ok(new ResponseObject("success", "Filtered course classes fetched successfully", dtos));
    }

    @GetMapping("/getTeacher/{courseClassId}")
    public ResponseEntity<ResponseObject> getTeacherById(@PathVariable Long courseClassId) {
        try {
            TeacherDto teacher= teacherDtoConverter.convert(courseClassService.getTeacherOfClass(courseClassId));
            return ResponseEntity.ok(new ResponseObject("success", "Teacher found successfully.", teacher)); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found teacher of class with id: " + courseClassId, null));
        } 
    }

    @GetMapping("/getClassOfTeacher")
    public ResponseEntity<ResponseObject> getClassOfTeacher(@RequestParam Long teacherId) {
        Set<CourseClassDto> dtos = courseClassDtoConverter.convert(courseClassService.getClassOfTeacher(teacherId));
        if(dtos.size()==0 )  return ResponseEntity.status(404).body(new ResponseObject("Not_found", "No class for this teacher id: "+ teacherId, null));
        return ResponseEntity.ok(new ResponseObject("success", "Course classes fetched successfully", dtos));
    }
    

//All Post Method
    @PostMapping
    public ResponseEntity<ResponseObject> createCourseClass(@RequestBody CreateCourseClass courseClassDto) {
    try {
        CourseClassDto courseClass = courseClassDtoConverter.convert(courseClassService.createCourseClass(courseClassDto));
        return ResponseEntity.ok(new ResponseObject("success", "Course class created successfully", courseClass));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
    }
}

//ALL Put Request
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCourse(@RequestBody UpdateCourseClass updateCourseClass) {
        try {
            CourseClassDto updatedCourse = courseClassDtoConverter.convert(courseClassService.updateCourse(updateCourseClass));
            return ResponseEntity.ok(new ResponseObject("success", "Update successful", updatedCourse));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found course with id: " + updateCourseClass.getId(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed to update data: " + e.getMessage(), null));
        }
    }


//All Delete Request
    @DeleteMapping("/{courseId}")
    public ResponseEntity<ResponseObject> deleteCourse(@PathVariable Long courseId) {
        try {
            courseClassService.deleteCourseClass(courseId);
            return ResponseEntity.ok(new ResponseObject("success", "Delete course class successful", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "Not found course class with id: " + courseId, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("failed", "Failed when delete course class: " + e.getMessage(), null));
        }
    }

    
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllCourses() {
        courseClassService.deleteAllCourses();
        return ResponseEntity.noContent().build();
    }




// Start new semester
    // 1: Mở đăng ký cho các khóa học ở Course controller
    // 2: Check xem có đủ giáo viên cho tất cả các lớp không ở CourseToRegistrationController
    // 3: Mở lớp cho tất cả học sinh dựa trên phiếu đăng ký ở CourseToRegistrationController
    // 4: Chính thức tất cả các lớp inProgres ở CouresClassController
    @GetMapping("/check")
    public ResponseEntity<ResponseObject> check() {
        if (courseClassService.check()) {
            return ResponseEntity.ok(new ResponseObject("success", "All course classes are ready to start new semester", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Not all course classes are ready to start new semester", null));
        }
    }

    @GetMapping("/start-new-semester")
    public ResponseEntity<ResponseObject> startNewSemester() {
        try {
            courseClassService.startNewSemester();
            // If successful, return a success message with no data payload
            return ResponseEntity.ok(new ResponseObject("success", "New semester started successfully, and all planned course classes are now in progress.", null));
        } catch (Exception e) {
            // In case of failure, return a detailed error message
            return ResponseEntity.internalServerError().body(new ResponseObject("error", "Failed to start new semester: " + e.getMessage(), null));
        }
    }

// End old semester ở CourseClassController
    @PostMapping("/endSemester")
    public ResponseEntity<ResponseObject> endSemester() {
        try {
            courseClassService.endSemester();
            return ResponseEntity.ok(new ResponseObject("success", "All in-progress course classes ended successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("error", "Failed to end semester: " + e.getMessage(), null));
        }
    }

}
