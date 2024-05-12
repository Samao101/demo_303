package com.example.demo.service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.Repositories.CourseClassRepository;
import com.example.demo.Repositories.CourseRepository;
import com.example.demo.Repositories.SheetMarkRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.SheetMarkDto;
import com.example.demo.models.DTOs.converter.SheetMarkDtoConverter;
import com.example.demo.models.DTOs.request.CourseClass.CreateCourseClass;
import com.example.demo.models.DTOs.request.CourseClass.UpdateCourseClass;
import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.Student;
import com.example.demo.models.entity.Teacher;
// import com.example.demo.models.entity.Teacher;
import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.SheetMarkStatus;
import com.example.demo.specification.CourseClassSpecification;

import jakarta.transaction.Transactional;

@Service
public class CourseClassService {
    public final SheetMarkDtoConverter sheetMarkDtoConverter;

    public final StudentRepository studentRepository;
    public final CourseRepository courseRepository;
    public final CourseClassRepository courseClassRepository;
    public final TeacherRepository teacherRepository;
    public final SheetMarkRepository sheetMarkRepository;

    public final StudentService studentService;

    public CourseClassService ( StudentService studentService,
                                CourseClassRepository courseClassRepository, 
                                SheetMarkDtoConverter sheetMarkDtoConverter,
                                CourseRepository courseRepository, 
                                TeacherRepository teacherRepository,
                                StudentRepository studentRepository,
                                SheetMarkRepository sheetMarkRepository) {
        this.courseClassRepository = courseClassRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.sheetMarkRepository = sheetMarkRepository;
        this.sheetMarkDtoConverter = sheetMarkDtoConverter;
        
        this.studentService = studentService;
    }

    // All get method serve for get request
    public Set<CourseClass> getAllCourseClasses() {
        return new HashSet<>(courseClassRepository.findAll());
    }

    public Optional<CourseClass> getClassById( Long id) {
        return courseClassRepository.findById(id);
    }

    public Set<CourseClass> getClassByCourseId(String courseId) {
        return courseClassRepository.findByCourseId(courseId);
    }

    public Set<CourseClass> getClassByCourseIdAndLevel( String courseId, CourseLevel courseLevel) {
        return courseClassRepository.findByCourseIdAndCourseLevel(courseId, courseLevel);
    }

    public Set<CourseClass> getClassByStatus( CourseClassStatus courseClassStatus) {
        return courseClassRepository.findByCourseClassStatus(courseClassStatus);
    }

    public Set<CourseClass> getCoursesByDayOfWeek(DayOfWeek dayOfWeek) {
        return courseClassRepository.findByDayOfWeek(dayOfWeek);
    }

    public Set<CourseClass> getClasses(String courseId, CourseLevel courseLevel, DayOfWeek dayOfWeek) {
        Specification<CourseClass> spec = CourseClassSpecification.findByCriteria(courseId, courseLevel, dayOfWeek);
        return new HashSet<>(courseClassRepository.findAll(spec));
    }

    public Set<SheetMarkDto> getAllSheetMarks(Long courseClassId) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseClassId));

        Set<SheetMark> sheetMarks = sheetMarkRepository.findByCourseClassId(courseClass.getId());
        return sheetMarkDtoConverter.convert(sheetMarks);
    }

    public Set<CourseClass> getCurrentClassOfStudent(Long studentId, CourseClassStatus classStatus) {
        return courseClassRepository.findByStudentIdsContainsAndCourseClassStatus(studentId, classStatus);
    }

    public Boolean checkClassInProgress() {
        Set<CourseClass> classes= new HashSet<>(courseClassRepository.findAll());
        for(CourseClass courseClass : classes) {
            if(courseClass.getCourseClassStatus() == CourseClassStatus.inProgress) return true;
        }
        return false;
    }

    // All post method serve for post request
    public CourseClass createCourseClass(CreateCourseClass courseClassDto) {
        Course course = courseRepository.findByCourseId(courseClassDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseClassDto.getCourseId()));
        Teacher teacher = teacherRepository.findById(courseClassDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id " +courseClassDto.getTeacherId()));

        CourseClass courseClass = new CourseClass();

        courseClass.setCourseId(course.getCourseId());
        courseClass.setCourseLevel(courseClassDto.getCourseLevel());
        courseClass.setStartPeriod(courseClassDto.getStartPeriod());
        courseClass.setEndPeriod(courseClassDto.getEndPeriod());
        courseClass.setDayOfWeek(courseClassDto.getDayOfWeek());
        courseClass.setTeacherId(teacher.getId());
        courseClass.setCapacity(courseClassDto.getCapacity());

        return courseClassRepository.save(courseClass);
    }

    // All Method Serve For Put Request
    @Transactional
    public CourseClass updateCourse(UpdateCourseClass courseDetailsDto) {
        CourseClass courseClass = courseClassRepository.findById(courseDetailsDto.getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseDetailsDto.getId()));

        Course course = courseRepository.findByCourseId(courseDetailsDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseDetailsDto.getCourseId()));

        Teacher teacher = teacherRepository.findById(courseDetailsDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id " +courseDetailsDto.getTeacherId()));

        if(!course.getCourseId().equals(courseClass.getCourseId())) {
            throw new IllegalArgumentException("Course id of course class can not be change!!!");
        }

        if(!courseDetailsDto.getCourseLevel().equals(courseClass.getCourseLevel())) {
            throw new IllegalArgumentException("Course level of course class can not be change!!!");
        }

        if(!teacher.getId().equals(courseClass.getTeacherId())) {
            throw new IllegalArgumentException("Teacher id of course class can not be change!!!");
        }

        courseClass.setStartPeriod(courseDetailsDto.getStartPeriod());
        courseClass.setEndPeriod(courseDetailsDto.getEndPeriod());
        courseClass.setDayOfWeek(courseDetailsDto.getDayOfWeek());
        courseClass.setCapacity(courseDetailsDto.getCapacity());
        courseClass.setCourseClassStatus(courseDetailsDto.getCourseClassStatus());

        return courseClassRepository.save(courseClass);
    }

    // All Method Serve For Delete Request
    public void deleteCourseClass(Long courseClassId) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseClassId));

        Set<SheetMark> sheetMarks = sheetMarkRepository.findByCourseClassId(courseClassId);
        for(SheetMark sheetMark : sheetMarks) {
            sheetMarkRepository.delete(sheetMark);
        }
        courseClassRepository.delete(courseClass);
    }

    public void deleteAllCourses() {
        courseRepository.deleteAll();
    } 

    //End class
    public void endCourseClass(Long courseClassId) {
        
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new IllegalArgumentException("Course class not found with id: " + courseClassId));

        Set<SheetMark> sheetMarks= sheetMarkRepository.findByCourseClassId(courseClassId);
        for(SheetMark sheetMark : sheetMarks) {
            sheetMark.finalSheetMark();
        }

        courseClass.endClass();
        courseClassRepository.save(courseClass);
    }

    public void endCourseClass(CourseClass courseClass) {
        Set<SheetMark> sheetMarks= sheetMarkRepository.findByCourseClassId(courseClass.getId());
        for(SheetMark sheetMark : sheetMarks) {
            sheetMark.finalSheetMark();
        }
        courseClass.endClass();
        courseClassRepository.save(courseClass);
    }


    //End semester
    public void endSemester() {
        Set<CourseClass> courseClasses = courseClassRepository.findByCourseClassStatus(CourseClassStatus.inProgress);
        for(CourseClass courseClass : courseClasses) {
            endCourseClass(courseClass);
        }
    }

    //Start new semester
    public void startNewSemester() {
        Set<CourseClass> courseClasses = courseClassRepository.findByCourseClassStatus(CourseClassStatus.inPlanned);
        for(CourseClass courseClass : courseClasses) {
            courseClass.setCourseClassStatus(CourseClassStatus.inProgress);
            Set<Long> studentIds= courseClass.getStudentIds();
            for(Long studentId : studentIds) {
                Optional<Student> student = studentRepository.findById(studentId);
                if(!student.isPresent()) continue;

                SheetMark sheetMark = new SheetMark();

                sheetMark.setCourseId(courseClass.getCourseId());
                sheetMark.setCourseClassId(courseClass.getId());
                sheetMark.setCourseLevel(courseClass.getCourseLevel());
                sheetMark.setStudentId(student.get().getId());
                sheetMark.setTeacherId(courseClass.getTeacherId());
                sheetMark.setSheetMarkStatus(SheetMarkStatus.inProgress);
                
                sheetMarkRepository.save(sheetMark);
            }
        }
    }


    //Get teacher of class
    public Teacher getTeacherOfClass(Long courseClassId) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new IllegalArgumentException("Course class not found with id: " + courseClassId));

        return teacherRepository.findById(courseClass.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + courseClass.getTeacherId()));
    }

    public boolean check() {
        Set<CourseClass> courseClasses = courseClassRepository.findByCourseClassStatus(CourseClassStatus.inPlanned);
        return (courseClasses.size() != 0);
    }

    public Set<CourseClass> getClassOfTeacher(Long teacherId) {
        return courseClassRepository.findByTeacherIdAndCourseClassStatus(teacherId, CourseClassStatus.inProgress);
    }
    
}
