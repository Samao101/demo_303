package com.example.demo.service;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.Repositories.AdminRepository;
import com.example.demo.Repositories.CourseClassRepository;
import com.example.demo.Repositories.CourseRepository;
import com.example.demo.Repositories.QualificationRepository;
import com.example.demo.Repositories.SheetMarkRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.CourseClassDto;
import com.example.demo.models.DTOs.converter.CourseClassDtoConverter;
import com.example.demo.models.DTOs.request.Teacher.CreateTeacherRequest;
import com.example.demo.models.DTOs.request.Teacher.UpdateTeacherRequest;
import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.Qualification;
import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.Teacher;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.enums.Role;

import jakarta.transaction.Transactional;

@Service
public class TeacherService {
    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;
    private final CourseClassRepository courseClassRepository;
    private final StudentRepository studentRepository;  
    private final TeacherRepository teacherRepository;
    private final QualificationRepository qualificationRepository;
    private final SheetMarkRepository sheetMarkRepository;

    private final CourseClassDtoConverter courseClassDtoConverter;

    private final UserService userService;
    private final CourseClassService courseClassService;


    public TeacherService(  CourseClassService courseClassService,
                            UserService userService,
                            AdminRepository adminRepository,
                            CourseRepository courseRepository,
                            StudentRepository studentRepository,
                            TeacherRepository teacherRepository,
                            CourseClassRepository courseClassRepository,
                            QualificationRepository qualificationRepository,
                            SheetMarkRepository sheetMarkRepository,
                            CourseClassDtoConverter courseClassDtoConverter) {
        this.adminRepository = adminRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseClassRepository = courseClassRepository;
        this.qualificationRepository = qualificationRepository;
        this.sheetMarkRepository = sheetMarkRepository;

        this.courseClassDtoConverter = courseClassDtoConverter;

        this.userService = userService;
        this.courseClassService= courseClassService;
    }


    // All method serve get methode
    public Set<Teacher> getAllTeachers() {
        return teacherRepository.findAlls();
    }

    public Set<Teacher> getAllTeachersIsAvailable() {
        return teacherRepository.findByIsAvailable(true);
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Set<Teacher> getTeacherByName(String name) {
        return teacherRepository.findByName(name);
    }

    public Set<Teacher> getTeachersByCourseId(String courseId) {
        return teacherRepository.findByMainCourseId(courseId);
    }

    public Set<Teacher> getTeachersByCourseIdAndIsAvailable(String courseId, Boolean isAvailable) {
        return teacherRepository.findByMainCourseIdAndIsAvailable(courseId, isAvailable);
    }

    public Set<CourseClassDto> getTeachingScheduleByDay(Long teacherId, DayOfWeek dayOfWeek) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        Set<CourseClass> courseClasses= new HashSet<>();
        for(Long cClassId: teacher.getCourseClassIds()) {
            Optional<CourseClass> courseClass= courseClassRepository.findById(cClassId);
            if(courseClass.isPresent()) courseClasses.add(courseClass.get());
        }

        return courseClasses.stream()
                .filter(courseClass -> courseClass.getDayOfWeek() == dayOfWeek)
                .map(courseClass -> {
                    return courseClassDtoConverter.convert(courseClass);
                })
                .collect(Collectors.toSet());
    }

    public Map<DayOfWeek, Set<CourseClassDto>> getWeeklyTeachingSchedule(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));
        
        Set<CourseClass> courseClasses= new HashSet<>();
        for(Long cClassId: teacher.getCourseClassIds()) {
            Optional<CourseClass> courseClass= courseClassRepository.findById(cClassId);
            if(courseClass.isPresent()) courseClasses.add(courseClass.get());
        }

        Map<DayOfWeek, Set<CourseClassDto>> scheduleMap = new EnumMap<>(DayOfWeek.class);
    
        // Lặp qua mỗi DayOfWeek và thu thập lịch dạy
        for (DayOfWeek day : DayOfWeek.values()) {
            Set<CourseClassDto> dailySchedule = courseClasses.stream()
                .filter(courseClass -> courseClass.getDayOfWeek() == day)
                .map(courseClass -> {
                    return courseClassDtoConverter.convert(courseClass);
                })
                .collect(Collectors.toSet());
            scheduleMap.put(day, dailySchedule);
        }
    
        return scheduleMap;
    }
    
    // All create method for post request
    public Teacher createTeacher(CreateTeacherRequest teacherRequest) {
        if (studentRepository.existsByCitizenIdentification(teacherRequest.getCitizenIdentification()) || 
            teacherRepository.existsByCitizenIdentification(teacherRequest.getCitizenIdentification()) ||
            adminRepository.existsByCitizenIdentification(teacherRequest.getCitizenIdentification())) {
            throw new IllegalArgumentException("This citizen identification already exists.");
        }
        
        if (studentRepository.existsByEmail(teacherRequest.getEmail()) ||
            teacherRepository.existsByEmail(teacherRequest.getEmail()) ||
            adminRepository.existsByEmail(teacherRequest.getEmail())) {
            throw new IllegalArgumentException("This email already exists.");
        }

        if (studentRepository.existsByPhoneNumber(teacherRequest.getPhoneNumber()) ||
            teacherRepository.existsByPhoneNumber(teacherRequest.getPhoneNumber()) ||
            adminRepository.existsByPhoneNumber(teacherRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("This phone number already exists.");
        }

        Course course= courseRepository.findByCourseId(teacherRequest.getMainCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + teacherRequest.getMainCourseId()));

        Teacher teacher = new Teacher();
        teacher.setName(teacherRequest.getName());
        teacher.setAge(teacherRequest.getAge());
        teacher.setGender(teacherRequest.getGender());
        teacher.setDateOfBirth(teacherRequest.getDateOfBirth());
        teacher.setPlaceOfBirth(teacherRequest.getPlaceOfBirth());
        teacher.setCitizenIdentification(teacherRequest.getCitizenIdentification());
        teacher.setEmail(teacherRequest.getEmail());
        teacher.setPhoneNumber(teacherRequest.getPhoneNumber());
        teacher.setIsAvailable(true);

        teacher.setMainCourseId(course.getCourseId());

        Teacher savedTeacher = teacherRepository.save(teacher);

        try {
            User teacherUser = userService.createUser("teacher" + savedTeacher.getId(), Role.TEACHER);
            savedTeacher.setUser(teacherUser);
        } catch (Exception e) {
            teacherRepository.delete(savedTeacher);
            throw new IllegalArgumentException("Could not create account: " + e.getMessage());
        }

        return teacherRepository.save(teacher);
    }

    public Set<Teacher> createTeachers(Set<CreateTeacherRequest> teacherRequest, Set<String> errors) {
        Set<Teacher> createdTeachers =new HashSet<>();
        for (CreateTeacherRequest request : teacherRequest) {
            try {
                Teacher teacher = createTeacher(request);
                createdTeachers.add(teacher);
            } catch (IllegalArgumentException e) {
                errors.add("Failed to create student " + request.getName() + ": " + e.getMessage());
            }
        }
        return createdTeachers;
    }

    // All update method for put request
    @Transactional
    public Teacher updateTeacher(UpdateTeacherRequest teacherRequest) {
        Long id= teacherRequest.getId();
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id " + id));
        Course course= courseRepository.findByCourseId(teacherRequest.getMainCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + teacherRequest.getMainCourseId()));

        teacherRepository.findByCitizenIdentification(teacherRequest.getCitizenIdentification())
        .filter(existingTeacher -> (!existingTeacher.getId().equals(id)) || 
                                    studentRepository.existsByCitizenIdentification(teacherRequest.getCitizenIdentification()))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Citizen Identification is already in use.");
        });

        studentRepository.findByEmail(teacherRequest.getEmail())
        .filter(existingTeacher -> (!existingTeacher.getId().equals(id) ||
                                    studentRepository.existsByEmail(teacherRequest.getEmail())))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Email is already in use.");
        });

        studentRepository.findByPhoneNumber(teacherRequest.getPhoneNumber())
        .filter(existingTeacher -> (!existingTeacher.getId().equals(id) ||
                                    studentRepository.existsByPhoneNumber(teacherRequest.getPhoneNumber())))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Phone number is already in use.");
        });
                    teacher.setName(teacherRequest.getName());
                    teacher.setAge(teacherRequest.getAge());
                    teacher.setGender(teacherRequest.getGender());
                    teacher.setDateOfBirth(teacherRequest.getDateOfBirth());
                    teacher.setPlaceOfBirth(teacherRequest.getPlaceOfBirth());
                    teacher.setCitizenIdentification(teacherRequest.getCitizenIdentification());
                    teacher.setEmail(teacherRequest.getEmail());
                    teacher.setPhoneNumber(teacherRequest.getPhoneNumber());

                    teacher.setIsAvailable(true);
                    teacher.setMainCourseId(course.getCourseId());
        return teacherRepository.save(teacher);
    }

    // End class
    public void endCoureClassOfTeacher(Long teacherId, Long courseClassId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id " + teacherId));
        
        Set<Long> courseClassIds= teacher.getCourseClassIds();
        if (courseClassIds.contains(courseClassId)) {
            courseClassService.endCourseClass(courseClassId);
            courseClassIds.remove(courseClassId);

            teacher.setCourseClassIds(courseClassIds);
            teacherRepository.save(teacher);
        } else {
            throw new IllegalArgumentException("This Course Class Id not belong to teacher!!!");
        }
    }

    public void endCoureClassOfTeacher(Teacher teacher, Long courseClassId) {
        Set<Long> courseClassIds= teacher.getCourseClassIds();
        if (courseClassIds.contains(courseClassId)) {
            courseClassService.endCourseClass(courseClassId);
            courseClassIds.remove(courseClassId);

            teacher.setCourseClassIds(courseClassIds);
            teacherRepository.save(teacher);
        } else {
            throw new IllegalArgumentException("This Course Class Id not belong to teacher!!!");
        }
    }

    public void endAllClassOfTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id " + teacherId));

        Set<Long> courseClassIds = teacher.getCourseClassIds();
        for(Long courseClassId : courseClassIds) {
            courseClassService.endCourseClass(courseClassId);
            courseClassIds.remove(courseClassId);
        }

        teacher.setCourseClassIds(courseClassIds);
        teacherRepository.save(teacher);
    }

    public void endAllClassOfTeacher(Teacher teacher) {
        Set<Long> courseClassIds = teacher.getCourseClassIds();
        for(Long courseClassId : courseClassIds) {
            courseClassService.endCourseClass(courseClassId);
            courseClassIds.remove(courseClassId);
        }

        teacher.setCourseClassIds(courseClassIds);
        teacherRepository.save(teacher);
    } 

    public void endAllCourseClass() {
        Set<Teacher> teachers= teacherRepository.findAlls();
        for(Teacher teacher : teachers) {
            endAllClassOfTeacher(teacher);
        }
    }

    public void endTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id :" + teacherId));

        teacher.setIsAvailable(false);
        teacherRepository.save(teacher);
    }

    // All delete method for delete request
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher found with id " + teacherId));
        Set<Qualification> qualifications = qualificationRepository.findByTeacherId(teacherId);
        for(Qualification qualification : qualifications) {
            qualificationRepository.delete(qualification);
        }

        Set<SheetMark> sheetMarks = sheetMarkRepository.findByTeacherId(teacherId);
        for(SheetMark sheetMark : sheetMarks) {
            sheetMarkRepository.delete(sheetMark);
        }

        Set<CourseClass> courseClasses = courseClassRepository.findByTeacherId(teacherId);
        for(CourseClass courseClass : courseClasses) {
            courseClassRepository.delete(courseClass);
        }
        
        teacherRepository.delete(teacher);
    }

    public void deleteAllTeacher() {
        Set<Teacher> teachers = teacherRepository.findAlls();
        for(Teacher teacher : teachers) teacherRepository.delete(teacher);
    }
}

