package com.example.demo.service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.Repositories.CourseClassRepository;
import com.example.demo.Repositories.CourseRepository;
import com.example.demo.Repositories.CourseToRegistrationRepository;
import com.example.demo.Repositories.SheetMarkRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.request.CourseToRegistration.CreateCourseToRegistration;
import com.example.demo.models.DTOs.request.CourseToRegistration.UpdateCourseToRegistration;
import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.CourseRecommendation;
import com.example.demo.models.entity.CourseToRegistration;
import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.Student;
import com.example.demo.models.entity.Teacher;
import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.SheetMarkStatus;
import com.example.demo.specification.CourseToRegistrationSpecification;

import jakarta.transaction.Transactional;

@Service
public class CourseToRegistrationService {
    private final SheetMarkRepository sheetMarkRepository;
    private final CourseToRegistrationRepository courseToRegistrationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseClassRepository courseClassRepository;
    private final TeacherRepository teacherRepository;



    public CourseToRegistrationService( SheetMarkRepository sheetMarkRepository,
                                        CourseClassRepository courseClassRepository,
                                        CourseToRegistrationRepository courseToRegistrationRepository, 
                                        CourseRepository courseRepository,
                                        TeacherRepository teacherRepository,
                                        StudentRepository studentRepository) {
        this.sheetMarkRepository = sheetMarkRepository;
        this.courseToRegistrationRepository = courseToRegistrationRepository;
        this.courseRepository = courseRepository;
        this.courseClassRepository = courseClassRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    // All get method serve for get request
    public Set<CourseToRegistration> getAll() {
        return courseToRegistrationRepository.findAlls();
    }

    public Optional<CourseToRegistration> getById(Long id) {
        return courseToRegistrationRepository.findById(id);
    }

    public Set<CourseToRegistration> getByCourseId(String courseId) {
        return courseToRegistrationRepository.findByCourseId(courseId);
    }

    public Set<CourseToRegistration> getByCourseIdAndLevel(String courseId, CourseLevel courseLevel) {
        return courseToRegistrationRepository.findByCourseIdAndCourseLevel(courseId, courseLevel);
    }

    public Set<CourseToRegistration> getCourseToRegistrations(String courseId, CourseLevel courseLevel, DayOfWeek dayOfWeek) {
        Specification<CourseToRegistration> spec = CourseToRegistrationSpecification.findByCriteria(courseId, courseLevel, dayOfWeek);
        return new HashSet<>(courseToRegistrationRepository.findAll(spec));
    }

    public Set<CourseToRegistration> getCourseForCheckEnoughTeacher(String courseId, Integer period , DayOfWeek dayOfWeek) {
        return courseToRegistrationRepository.findByCourseIdAndPeriodAndDayOfWeek(courseId, period, dayOfWeek);
    }

    public Set<CourseToRegistration> getAllRegistrationOfStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id " + studentRepository));
        return courseToRegistrationRepository.findAllByStudentId(student.getId());
    }

    public Set<CourseRecommendation> getSuitableRegistForStudent(Long studentId) {
        Set<Course> courses= courseRepository.findAlls();
        Set<CourseRecommendation> courseToRegistrations= new HashSet<>();
        for(Course course : courses) {
            Set<SheetMark> sheetMarks = sheetMarkRepository.findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(studentId, course.getCourseId(), CourseLevel.ADVANCED, SheetMarkStatus.Completed_in_pass);
            if(sheetMarks.size() != 0) continue;

            sheetMarks = sheetMarkRepository.findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(studentId, course.getCourseId(), CourseLevel.ADVANCED, SheetMarkStatus.inProgress);
            if(sheetMarks.size() != 0) continue;

            sheetMarks = sheetMarkRepository.findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(studentId, course.getCourseId(), CourseLevel.INTERMEDIATE, SheetMarkStatus.Completed_in_pass);
            if(sheetMarks.size() != 0) {
                courseToRegistrations.add(new CourseRecommendation(course.getCourseId(), CourseLevel.ADVANCED));
                continue;
            }

            sheetMarks = sheetMarkRepository.findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(studentId, course.getCourseId(), CourseLevel.INTERMEDIATE, SheetMarkStatus.Completed_in_pass);
            if(sheetMarks.size() != 0) {
                courseToRegistrations.add(new CourseRecommendation(course.getCourseId(), CourseLevel.ADVANCED));
                continue;
            }

            sheetMarks = sheetMarkRepository.findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(studentId, course.getCourseId(), CourseLevel.BEGINNER, SheetMarkStatus.Completed_in_pass);
            if(sheetMarks.size() != 0) {
                courseToRegistrations.add(new CourseRecommendation(course.getCourseId(), CourseLevel.INTERMEDIATE));
                continue;
            }

            sheetMarks = sheetMarkRepository.findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(studentId, course.getCourseId(), CourseLevel.BEGINNER, SheetMarkStatus.Completed_in_pass);
            if(sheetMarks.size() != 0) {
                courseToRegistrations.add(new CourseRecommendation(course.getCourseId(), CourseLevel.INTERMEDIATE));
                continue;
            }

            courseToRegistrations.add(new CourseRecommendation(course.getCourseId(), CourseLevel.BEGINNER));
        }

        return courseToRegistrations;
    }

// All post method serve for post request
    public CourseToRegistration createCourse(CreateCourseToRegistration courseDto) throws IllegalArgumentException {
        if (courseToRegistrationRepository.existsByCourseIdAndCourseLevelAndStartPeriodAndEndPeriod(
                courseDto.getCourseId(), courseDto.getCourseLevel(), courseDto.getStartPeriod(), courseDto.getEndPeriod())) {
            throw new IllegalArgumentException("A registration for this course with the same level, start and end period already exists.");
        }

        CourseToRegistration course = new CourseToRegistration();
        courseRepository.findById(courseDto.getCourseId()).orElseThrow(
            () -> new IllegalArgumentException("Course not found with id: " + courseDto.getCourseId()));
        course.setCourseId(courseDto.getCourseId());
        course.setCourseLevel(courseDto.getCourseLevel());
        course.setDayOfWeek(courseDto.getDayOfWeek());
        course.setStartPeriod(courseDto.getStartPeriod());
        course.setEndPeriod(courseDto.getEndPeriod());
        course.setMaxRegist(courseDto.getMaxRegist());
        course.setMaxRegistClass(courseDto.getMaxRegistClass());

        return courseToRegistrationRepository.save(course);
    }

    public Set<CourseToRegistration> createMultipleCourses(Set<CreateCourseToRegistration> courseDtoSet, Set<String> errors) {
        Set<CourseToRegistration> createdCourses = new HashSet<>();
        for (CreateCourseToRegistration courseDto : courseDtoSet) {
            try {
               createCourse(courseDto);
            } catch (Exception e) {
                errors.add("Failed to create registration for course ID " + courseDto.getCourseId() + ": " + e.getMessage());
            }
        }
        return createdCourses;
    }

// All put method server for Put request
    @Transactional
    public CourseToRegistration updateCourse(UpdateCourseToRegistration courseDto) {
        CourseToRegistration courseToRegistration = courseToRegistrationRepository.findById(courseDto.getId())
                    .orElseThrow(() -> new RuntimeException("Course to regist not found with id: " + courseDto.getId()));

        Course course = courseRepository.findByCourseId(courseDto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseDto.getCourseId()));
        
        if(!courseDto.getCourseId().equals(course.getCourseId())) {
            throw new IllegalArgumentException("Course id of this course to registration can not be change!!!");
        }

        if(!courseDto.getCourseLevel().equals(courseToRegistration.getCourseLevel())) {
            throw new IllegalArgumentException("Course level of this course to registration can not be change!!!");
        }
        
        courseToRegistration.setDayOfWeek(courseDto.getDayOfWeek());
        courseToRegistration.setStartPeriod(courseDto.getStartPeriod());
        courseToRegistration.setEndPeriod(courseDto.getEndPeriod());
        courseToRegistration.setMaxRegist(courseDto.getMaxRegist());
        courseToRegistration.setMaxRegistClass(courseDto.getMaxRegistClass());

       return courseToRegistrationRepository.save(courseToRegistration);
    }

    public Set<CourseToRegistration> updateCourses(Set<UpdateCourseToRegistration> courseDtos, Set<String> errors) {
        Set<CourseToRegistration> updatedCourses = new HashSet<>();
        
        for (UpdateCourseToRegistration courseDto : courseDtos) {
            try {
                updatedCourses.add(updateCourse(courseDto));
            } catch (Exception e) {
                errors.add("Failed to update course with ID " + courseDto.getId() + ": " + e.getMessage());
            }
        }
        return updatedCourses; // For simplicity, only returning updated courses; consider handling errors more robustly.
    }

    // All delete method serve for Delete Request
    public void deleteById(Long id) {
        CourseToRegistration course = courseToRegistrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course To Registration not found with id " + id));
        courseToRegistrationRepository.delete(course);
    }

    public void deleteAllCourseToRegistration() {
        courseToRegistrationRepository.deleteAll();
    }

    // Check enough teacher number for school
    public Set<String> checkForTeacher() {
        Set<String> courseIds= new HashSet<>();
        Set<Course> courses = courseRepository.findAlls();
        for(Course course : courses) {
            Set<Teacher> teachers = teacherRepository.findByMainCourseIdAndIsAvailable(course.getCourseId(), true);
            Integer isAvailable= teachers.size();
            for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
                for(Integer period = 1; period <= 12 ;period ++) {
                    Set<CourseToRegistration> cToRegistrations= getCourseForCheckEnoughTeacher(course.getCourseId(), period, dayOfWeek);
                    Integer cur= 0;
                    for(CourseToRegistration cToRegistration : cToRegistrations) {
                        cur+= (cToRegistration.getStudents().size() + cToRegistration.getMaxRegistClass() - 1)/ cToRegistration.getMaxRegistClass();
                    }
                    if(cur > isAvailable) courseIds.add(course.getCourseId());
                }
            }
        }

        return courseIds;
    }

//Set up class with registration
    public void endRegistration() {
        Set<CourseToRegistration> courseToRegistrations = courseToRegistrationRepository.findAlls();
        for(CourseToRegistration courseToRegistration : courseToRegistrations) {
            processCourseRegistrations(courseToRegistration);
        }
        deleteAllCourseToRegistration();
    }

    public void processCourseRegistrations(CourseToRegistration ctr) {
        Set<Long> studentIds = ctr.getStudents();
        List<Student> registeredStudents = new ArrayList<>();
        for(Long studentId : studentIds) {
            Optional<Student> student = studentRepository.findById(studentId);
            if(student.isPresent()) registeredStudents.add(student.get());
        }

        Integer totalStudents = registeredStudents.size();
        Integer maxClassSize = ctr.getMaxRegistClass();
        Integer numberOfClassesNeeded = (totalStudents +maxClassSize - 1)/ maxClassSize;
        if(numberOfClassesNeeded == 0) return;
        Integer baseClassSize = totalStudents / numberOfClassesNeeded;
        Integer remainder = totalStudents % numberOfClassesNeeded;
        
        Set<Set<Student>> classes = new HashSet<>();
        Integer startIndex = 0;   

        // Distribute students into classes
        for (int i = 0; i < numberOfClassesNeeded; i++) {
            int thisClassSize = baseClassSize + (i < remainder ? 1 : 0);
            Set<Student> studentsForClass = new HashSet<> (registeredStudents.subList(startIndex, startIndex + thisClassSize));
            classes.add(studentsForClass);
            startIndex += thisClassSize;
        }

        Set<Teacher> availableTeachers = teacherRepository.findByMainCourseIdAndIsAvailable(ctr.getCourseId(), true);


        for (Set<Student> classGroup : classes) {
            if(classGroup.size() == 0) continue;
            Teacher assignedTeacher = findAvailableTeacher(availableTeachers, ctr.getDayOfWeek(),ctr.getStartPeriod(), ctr.getEndPeriod());
            if (assignedTeacher == null) {
                continue;
            }

            for(Student student : classGroup) {
                Set<CourseClass> curSet= courseClassRepository.findOverlappingClassesForStudent(student.getId(),CourseClassStatus.inPlanned, ctr.getDayOfWeek(), ctr.getStartPeriod(), ctr.getEndPeriod());
                if(curSet.size() != 0) classGroup.remove(student);
            }
            if(classGroup.size() == 0) continue;


            Set<Long> curIds= new HashSet<>();
            for(Student cur: classGroup) curIds.add(cur.getId());

            CourseClass newClass = new CourseClass();
            newClass.setCourseId(ctr.getCourseId());
            newClass.setTeacherId(assignedTeacher.getId());
            newClass.setStudentIds(curIds);
            newClass.setStartPeriod(ctr.getStartPeriod());
            newClass.setEndPeriod(ctr.getEndPeriod());
            newClass.setCapacity(ctr.getMaxRegistClass());
            newClass.setCourseClassStatus(CourseClassStatus.inPlanned);
            newClass.setDayOfWeek(ctr.getDayOfWeek());
            newClass.setCourseLevel(ctr.getCourseLevel());
            courseClassRepository.save(newClass);
        }
    }

    private Teacher findAvailableTeacher(Set<Teacher> teachers, DayOfWeek dayOfWeek,Integer startPeriod, Integer endPeriod) {
        Teacher cur = null;
        Integer curClass= 9999;
        for (Teacher teacher : teachers) {
            Set<CourseClass> overlappingClasses = courseClassRepository.findOverlappingClassesForTeacher(teacher.getId(),CourseClassStatus.inPlanned,dayOfWeek , startPeriod, endPeriod);
            if (overlappingClasses.isEmpty()) {
                if(cur == null) {
                    cur = teacher;
                    curClass= teacher.getCourseClassIds().size();
                } else {
                    if ( curClass >teacher.getCourseClassIds().size() ) {
                        cur = teacher;
                        curClass = teacher.getCourseClassIds().size();
                    }
                }
            }
        }
        
        return cur;
    }

    public boolean checkRegistration() {
        Set<CourseToRegistration> courseToRegistrations = courseToRegistrationRepository.findAlls();
        return (courseToRegistrations.size() != 0);
    }
}

