package com.example.demo.service;

import com.example.demo.Repositories.CourseClassRepository;
import com.example.demo.Repositories.CourseRepository;
import com.example.demo.Repositories.CourseToRegistrationRepository;
import com.example.demo.Repositories.SheetMarkRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.request.Course.BaseCourseRequest;
import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.CourseToRegistration;
import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.Teacher;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.specification.CourseSpecification;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseService {
    private final SheetMarkRepository sheetMarkRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseToRegistrationRepository courseToRegistrationRepository;
    private final CourseClassRepository courseClassRepository;

    public CourseService(   CourseRepository courseRepository, 
                            CourseToRegistrationRepository courseToRegistrationRepository,
                            SheetMarkRepository sheetMarkRepository,
                            TeacherRepository teacherRepository,
                            CourseClassRepository courseClassRepository) {
        this.sheetMarkRepository = sheetMarkRepository;
        this.courseRepository = courseRepository;
        this.courseToRegistrationRepository= courseToRegistrationRepository;
        this.teacherRepository = teacherRepository;
        this.courseClassRepository = courseClassRepository;
    }

    // All method to serve Get Request
    public Set<Course> getAllCourses() {
        return courseRepository.findAlls();
    }

    public Optional<Course> getCourseByCourseId(String courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    public Optional<Course> getCoursesByCourseName(String courseName) {
        return courseRepository.findByCourseName(courseName);
    }

    public Set<Course> getCourseByIsAvailable(Boolean isAvailable) {
        return courseRepository.findByIsAvailable(isAvailable);
    }

    public Set<Course> getCoursesByDayOfWeek(DayOfWeek dayOfWeek) {
        return courseRepository.findByClassDays(dayOfWeek);
    }

    public Set<Course> getCourses(String courseId, String courseName, Boolean isAvailable) {
        Specification<Course> spec = CourseSpecification.findByCriteria(courseId, courseName, isAvailable);
        return new HashSet<>(courseRepository.findAll(spec));
    }


    // ALl method to serve Post method

    public Course createCourse(BaseCourseRequest courseDto) {
            Optional<Course> courseOptional= courseRepository.findByCourseId(courseDto.getCourseId());
            if (courseOptional.isPresent()) {
                throw new RuntimeException("This course id already exists.");
            }
            courseOptional = courseRepository.findByCourseName(courseDto.getCourseName());
            if (courseOptional.isPresent()) {
                throw new RuntimeException("This course name already exists.");
            }

            Course course = new Course();

            course.setCourseId(courseDto.getCourseId());
            course.setCourseName(courseDto.getCourseName());
            course.setClassDays(courseDto.getClassDays());
            course.setMaxRegistPerRegist(courseDto.getMaxRegistPerRegist());
            course.setMaxStuPerClass(courseDto.getMaxStuPerClass());
            course.setIsAvailable(true);

            return courseRepository.save(course);
    }

    public void createCourses(Set<BaseCourseRequest> courseDtos, Set<String> successMessages, Set<String> errorMessages) {
        for (BaseCourseRequest courseDto : courseDtos) {
            try {
                createCourse(courseDto); // This method now throws an exception on failure
                successMessages.add("Successfully created course: " + courseDto.getCourseName());
            } catch (Exception e) {
                errorMessages.add("Failed to create course " + courseDto.getCourseName() + ": " + e.getMessage());
            }
        }
    }

    public void createRegistrationsForAllCourses() {
        Set<CourseToRegistration> courseToRegistrations = courseToRegistrationRepository.findAlls();
        if(courseToRegistrations.size() != 0) {
            throw new RuntimeException("Course to registration have already create!!!");
        }
        Set<Course> courses = courseRepository.findByIsAvailable(true);
        courses.forEach(this::createRegistrationsForCourse);
    }

    private void createRegistrationsForCourse(Course course) {
        Set<CourseToRegistration> registrations = new HashSet<>();
        for (DayOfWeek day : course.getClassDays()) {
            for (CourseLevel level : CourseLevel.values()) {
                if (level == CourseLevel.BEGINNER) {
                    registrations.addAll(createBasicRegistrations(course, day));
                } else {
                    registrations.addAll(createAdvancedRegistrations(course, day, level));
                }
            }
        }
        courseToRegistrationRepository.saveAll(registrations);
    }

    private Set<CourseToRegistration> createBasicRegistrations(Course course, DayOfWeek day) {
        Set<CourseToRegistration> registrations = new HashSet<>();
        int[][] periods = new int[][]{{2, 3}, {4, 5}, {7, 8},{9, 10}};
        for (int[] period : periods) {
            registrations.add(createRegistration(course, day, CourseLevel.BEGINNER, period[0], period[1]));
        }
        return registrations;
    }

    private Set<CourseToRegistration> createAdvancedRegistrations(Course course, DayOfWeek day, CourseLevel level) {
        Set<CourseToRegistration> registrations = new HashSet<>();
        int[][] periods = new int[][]{{2, 4}, {6, 8}, {9, 11}};
        for (int[] period : periods) {
            registrations.add(createRegistration(course, day, level, period[0], period[1]));
        }
        return registrations;
    }

    private CourseToRegistration createRegistration(Course course, DayOfWeek day, CourseLevel level, int start, int end) {
        CourseToRegistration registration = new CourseToRegistration();

        registration.setCourseId(course.getCourseId());
        registration.setDayOfWeek(day);
        registration.setCourseLevel(level);
        registration.setStartPeriod(start);
        registration.setEndPeriod(end);
        registration.setMaxRegist(course.getMaxRegistPerRegist());
        registration.setMaxRegistClass(course.getMaxStuPerClass());
        
        return registration;
    }

// All Method Serve For Put Request
    @Transactional
    public Course updateCourse(BaseCourseRequest courseDto) {
        Course course = courseRepository.findById(courseDto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + courseDto.getCourseId()));

        if(!courseDto.getCourseName().equals(course.getCourseName())) {
            throw new IllegalArgumentException("Course name of this course can not be change!!!");
        }

        course.setClassDays(courseDto.getClassDays());
        course.setMaxRegistPerRegist(courseDto.getMaxRegistPerRegist());
        course.setMaxStuPerClass(courseDto.getMaxStuPerClass());

        return courseRepository.save(course);
    }

    public void updateCourses(Set<BaseCourseRequest> courseRequests, Set<String> successes, Set<String> errors) {
        for (BaseCourseRequest courseRequest : courseRequests) {
            try {
                Course course = updateCourse(courseRequest);
                successes.add("Successfully updated course: " + course.getCourseName());
            } catch (Exception e) {
                errors.add("Failed to update course " + courseRequest.getCourseName() + ": " + e.getMessage());
            }
        }
    }

    public void endCourse(String courseId) {
        Course course= courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with coures id: "+ courseId));
        course.setIsAvailable(false);
        courseRepository.save(course);
    } 

    // All Method Serve For Delete Request
    public void deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + courseId));

        Set<CourseToRegistration> courseToRegistrations = courseToRegistrationRepository.findByCourseId(courseId);
        for(CourseToRegistration courseToRegistration : courseToRegistrations) {
            courseToRegistrationRepository.delete(courseToRegistration);
        }

        Set<SheetMark> sheetMarks = sheetMarkRepository.findByCourseId(courseId);
        for(SheetMark sheetMark : sheetMarks) {
            sheetMarkRepository.delete(sheetMark);
        }

        Set<Teacher> teachers = teacherRepository.findByMainCourseId(courseId);
        for(Teacher teacher : teachers) {
            teacher.setMainCourseId(null);
            teacherRepository.save(teacher);
        }

        Set<CourseClass> courseClasses = courseClassRepository.findByCourseId(courseId);
        for(CourseClass courseClass : courseClasses) {
            Set<SheetMark> sheetMarkInClass = sheetMarkRepository.findByCourseClassId(courseClass.getId());
            for(SheetMark sheetMark : sheetMarkInClass) {
                sheetMarkRepository.delete(sheetMark);
            }
            courseClassRepository.delete(courseClass);
        }

        courseRepository.delete(course);
    }

    public void deleteAllCourses() {
        Set<Course> courses = courseRepository.findAlls();
        for(Course course : courses) {
            deleteCourse(course.getCourseId());
        }
    }

}
