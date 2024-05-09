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
import com.example.demo.Repositories.CourseToRegistrationRepository;
import com.example.demo.Repositories.SheetMarkRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.CourseClassDto;
import com.example.demo.models.DTOs.StudentCreditsAndGPADTO;
import com.example.demo.models.DTOs.converter.CourseClassDtoConverter;
import com.example.demo.models.DTOs.request.Student.CreateStudentRequest;
import com.example.demo.models.DTOs.request.Student.UpdateStudentRequest;
import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.CourseToRegistration;
import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.Student;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.Role;
import com.example.demo.models.entity.enums.SheetMarkStatus;

@Service
public class StudentService {

    private final CourseClassDtoConverter courseClassDtoConverter;
    
    private final AdminRepository adminRepository;
    private final SheetMarkRepository sheetMarkRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseToRegistrationRepository courseToRegistrationRepository;
    private final CourseClassRepository courseClassRepository;
    private final CourseRepository courseRepository;

    private final UserService userService;
    
    public StudentService(  SheetMarkRepository sheetMarkRepository ,
                            StudentRepository studentRepository,
                            AdminRepository adminRepository,
                            TeacherRepository teacherRepository,
                            CourseClassDtoConverter courseClassDtoConverter,
                            CourseClassRepository courseClassRepository,
                            CourseToRegistrationRepository courseToRegistrationRepository,
                            CourseRepository courseRepository,
                            UserService userService) {
        this.courseClassDtoConverter = courseClassDtoConverter;
        
        this.adminRepository = adminRepository;
        this.courseClassRepository = courseClassRepository;
        this.courseToRegistrationRepository = courseToRegistrationRepository;
        this.sheetMarkRepository = sheetMarkRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository= courseRepository;

        this.userService = userService;

    }


    // All get method for get request
    public Set<Student> getAllStudents() {
        return studentRepository.findAlls();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Set<Student> getStudentByName(String name) {
        return studentRepository.findByName(name);
    }

    public Set<CourseClassDto> getStudyingSchedule(Long studentId, DayOfWeek dayOfWeek) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        Set<CourseClass> courseClasses= courseClassRepository.findByStudentIdsContainsAndDayOfWeekAndCourseClassStatus(student.getId(), dayOfWeek, CourseClassStatus.inProgress);
        return courseClasses.stream()
                .filter(courseClass -> courseClass.getDayOfWeek() == dayOfWeek)
                .map(courseClass -> {
                    return courseClassDtoConverter.convert(courseClass);
                })
                .collect(Collectors.toSet());
    }

    public Map<DayOfWeek, Set<CourseClassDto>> getWeeklyStudyingSchedule(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
    
        Map<DayOfWeek, Set<CourseClassDto>> scheduleMap = new EnumMap<>(DayOfWeek.class);
    
        // Lặp qua mỗi DayOfWeek và thu thập lịch dạy
        Set<CourseClass> courseClasses= courseClassRepository.findByStudentIdsContainsAndCourseClassStatus(student.getId(), CourseClassStatus.inProgress);

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

    public StudentCreditsAndGPADTO calculateCreditsAndGPA(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        Set<Course> courses = courseRepository.findAlls();

        int numberOfSheetMark= 0;
        double totalCredits = 0;
        double totalPointsForGPA = 0;
        double totalWeightedPoints = 0;

        for(Course course : courses ) {
            for(CourseLevel courselLevel : CourseLevel.values()) {
                Set<SheetMark> sheetMarks = sheetMarkRepository.findByCourseIdAndCourseLevelAndStudentIdAndSheetMarkStatus(course.getCourseId(), courselLevel, studentId, SheetMarkStatus.Completed_in_pass);
                if(sheetMarks.size() == 0) continue;
                SheetMark cur= null;
                for(SheetMark sheetMark : sheetMarks) {
                    if(cur == null) {
                        cur = sheetMark;
                    } else {
                        if(sheetMark.getFinalGrade() > cur.getFinalGrade()) {
                            cur= sheetMark;
                        }
                    }
                }
                
                double credits = courselLevel == CourseLevel.BEGINNER ? 3 : 4;
                totalCredits += credits;
                numberOfSheetMark+= 1;
                totalPointsForGPA += cur.getFinalGrade();
                totalWeightedPoints += cur.getFinalGrade() * credits;
            }
        }

        double gpaOnScaleTen = (numberOfSheetMark== 0) ? 0 : totalPointsForGPA / numberOfSheetMark;
        double gpaOnScaleFour = (numberOfSheetMark== 0) ? 0 : (totalWeightedPoints / totalCredits) / 10 * 4;

        return new StudentCreditsAndGPADTO(totalCredits, gpaOnScaleTen, gpaOnScaleFour);
    }
 
    public Set<SheetMark> getCurrentMark(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

            return sheetMarkRepository.findByStudentIdAndSheetMarkStatus(studentId, SheetMarkStatus.inProgress);
    }

    // All method for put request
    public Student createStudent(CreateStudentRequest studentRequest){
        if (studentRepository.existsByCitizenIdentification(studentRequest.getCitizenIdentification()) || 
            teacherRepository.existsByCitizenIdentification(studentRequest.getCitizenIdentification()) ||
            adminRepository.existsByCitizenIdentification(studentRequest.getCitizenIdentification())) {
            throw new IllegalArgumentException("This citizen identification already exists.");
        }
        
        if (studentRepository.existsByEmail(studentRequest.getEmail()) ||
            teacherRepository.existsByEmail(studentRequest.getEmail()) ||
            adminRepository.existsByEmail(studentRequest.getEmail())) {
            throw new IllegalArgumentException("This email already exists.");
        }

        if (studentRepository.existsByPhoneNumber(studentRequest.getPhoneNumber()) ||
            teacherRepository.existsByPhoneNumber(studentRequest.getPhoneNumber()) ||
            adminRepository.existsByPhoneNumber(studentRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("This phone number already exists.");
        }

        Student student = new Student();

        student.setName(studentRequest.getName());
        student.setAge(studentRequest.getAge());
        student.setGender(studentRequest.getGender());
        student.setDateOfBirth(studentRequest.getDateOfBirth());
        student.setPlaceOfBirth(studentRequest.getPlaceOfBirth());
        student.setCitizenIdentification(studentRequest.getCitizenIdentification());
        student.setEmail(studentRequest.getEmail());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setIsStudy(true);

        Student savedStudent = studentRepository.save(student);

        try {
            User studentUser = userService.createUser("student_" + savedStudent.getId(), Role.STUDENT);
            savedStudent.setUser(studentUser);
        } catch (Exception e) {
            studentRepository.delete(savedStudent);
            throw new IllegalArgumentException("Could not create account: " + e.getMessage());
        }
       
        
        return studentRepository.save(student);
    }

    public Set<Student> createStudents(Set<CreateStudentRequest> studentRequests, Set<String> errors) {
        Set<Student> createdStudents = new HashSet<>();
        for (CreateStudentRequest request : studentRequests) {
            try {
                Student student = createStudent(request);
                createdStudents.add(student);
            } catch (IllegalArgumentException e) {
                errors.add("Failed to create student " + request.getName() + ": " + e.getMessage());
            }
        }
        return createdStudents;
    }

    // All put method for put request
    public Student updateStudent(UpdateStudentRequest updatedStudent) {
        Long id= updatedStudent.getId();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        studentRepository.findByCitizenIdentification(updatedStudent.getCitizenIdentification())
        .filter(existingStudent -> (!existingStudent.getId().equals(id)) || 
                                    teacherRepository.existsByCitizenIdentification(updatedStudent.getCitizenIdentification()))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Citizen Identification is already in use.");
        });

        studentRepository.findByEmail(updatedStudent.getEmail())
        .filter(existingStudent -> (!existingStudent.getId().equals(id) ||
                                    teacherRepository.existsByEmail(updatedStudent.getEmail())))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Email is already in use.");
        });

        studentRepository.findByPhoneNumber(updatedStudent.getPhoneNumber())
        .filter(existingStudent -> (!existingStudent.getId().equals(id) ||
                                    teacherRepository.existsByPhoneNumber(updatedStudent.getPhoneNumber())))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Phone number is already in use.");
        });
        
        student.setName(updatedStudent.getName());
        student.setAge(updatedStudent.getAge());
        student.setGender(updatedStudent.getGender());
        student.setDateOfBirth(updatedStudent.getDateOfBirth());
        student.setPlaceOfBirth(updatedStudent.getPlaceOfBirth());
        student.setCitizenIdentification(updatedStudent.getCitizenIdentification());
        student.setEmail(updatedStudent.getEmail());
        student.setPhoneNumber(updatedStudent.getPhoneNumber());
        student.setIsStudy(updatedStudent.getIsStudy());

        return studentRepository.save(student);
    }

    public Set<Student> updateStudents(Set<UpdateStudentRequest> studentRequests, Set<String> errors) {
        Set<Student> updatedStudents = new HashSet<>();
        for (UpdateStudentRequest request : studentRequests) {
            try {
                Student student = updateStudent(request);
                updatedStudents.add(student);
            } catch (Exception e) {
                errors.add("Failed to update student " + request.getName() + ": " + e.getMessage());
            }
        }
        return updatedStudents;
    }

    public void endStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id :" + studentId));

        student.setIsStudy(false);
        studentRepository.save(student);
    }

    // All delete method for delete request
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + studentId));
        
        Set<SheetMark> sheetMarks = sheetMarkRepository.findByStudentId(studentId);
        for(SheetMark sheetMark : sheetMarks) {
            sheetMarkRepository.delete(sheetMark);
        }

        Set<CourseClass> courseClasses = courseClassRepository.findByStudentIdsContains(studentId);
        for(CourseClass courseClass : courseClasses) {
            Set<Long> studentIds = courseClass.getStudentIds();
            studentIds.remove(studentId);
            courseClass.setStudentIds(studentIds);
            courseClassRepository.save(courseClass);
        }

        studentRepository.delete(student);
    }

    public void deleteAllStudent() {
        Set<Student> students = studentRepository.findAlls();
        for(Student student : students) {
            deleteStudent(student.getId());
        }
    }

    // Regist method for regist to Course To Registration
    public String registerStudentToCourse(Long registrationId, Long studentId) {
        CourseToRegistration registration = courseToRegistrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration for course not found"));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student course not found"));
            
        // Check if registration is full
        if (registration.getStudents().size() >= registration.getMaxRegist()) {
            return "Registration is full";
        }

        // Check if the student is already registered for this course
        if(courseToRegistrationRepository.findByStudentIdAndCourseLevelAndCourseId(studentId, registration.getCourseLevel(), registration.getCourseId()).size() != 0) {
            return "Student with id "+ studentId+ " has already regist to the same course id and level!!!";
        }



        // Check prerequisites
        CourseLevel requiredLevel = registration.getCourseLevel();
        if(requiredLevel != CourseLevel.BEGINNER) {
            if(requiredLevel == CourseLevel.INTERMEDIATE) requiredLevel = CourseLevel.BEGINNER;
            if(requiredLevel == CourseLevel.ADVANCED) requiredLevel = CourseLevel.INTERMEDIATE;
            Set<SheetMark> marks = sheetMarkRepository.findByStudentIdAndCourseId(studentId, registration.getCourseId());

            // Assume CourseLevel is an enum where the ordinal value indicates the level (higher ordinal, higher level)
            boolean hasCompletedPrerequisite = false;
            for(SheetMark mark : marks) {
                if( mark.getCourseLevel() == requiredLevel && ((mark.getSheetMarkStatus() == SheetMarkStatus.Completed_in_pass) ||(mark.getSheetMarkStatus() == SheetMarkStatus.inProgress) ) ) {
                    hasCompletedPrerequisite = true;
                    break;
                }
            }

            if (!hasCompletedPrerequisite) {
                return "Student has not completed the necessary prerequisites";
            }
        }

        // Register the student
        registration.addStudentId(student);
        courseToRegistrationRepository.save(registration);

        return "Student registered successfully";
    }

    // Cancel Regist method for regist to Course To Registration
    public String cancelRegisterStudentToCourse(Long registrationId, Long studentId) {
        CourseToRegistration registration = courseToRegistrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration for course not found"));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student course not found"));
        
        Set<Long> studentIds= registration.getStudents();
        if(!studentIds.contains(studentId)) throw new RuntimeException("Student with id "+ student.getId() + " is not belong to regist with id " + registrationId+ " for course "+ registration.getCourseId()+ " !!!");
        
        studentIds.remove(studentId);
        registration.setStudents(studentIds);
        courseToRegistrationRepository.save(registration);

        return "Cancel student registration successfully";
    }

    // Regist method for regist to Course Class
    public String registerStudentToClass(Long classId, Long studentId) {
        CourseClass courseClass = courseClassRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student course not found"));
            
        // Check if registration is full
        if (courseClass.getStudentCount() >= courseClass.getCapacity()) {
            return "Class is full";
        }

        Set<CourseClass> courseClasses = courseClassRepository.findCourseClassesByStudentIdAndCourseIdAndCourseLevelAndStatus(studentId, courseClass.getCourseId(), courseClass.getCourseLevel(), CourseClassStatus.inPlanned);
        if(!courseClasses.isEmpty()) {
            return "Student with id "+ studentId + " have already regist to another class with same course id and cousre level !!!";
        }

        // Check prerequisites
        CourseLevel requiredLevel = courseClass.getCourseLevel();
        if(requiredLevel != CourseLevel.BEGINNER) {
            if(requiredLevel == CourseLevel.INTERMEDIATE) requiredLevel = CourseLevel.ADVANCED;
            if(requiredLevel == CourseLevel.ADVANCED) requiredLevel = CourseLevel.INTERMEDIATE;
            Set<SheetMark> marks = sheetMarkRepository.findByStudentIdAndCourseId(studentId, courseClass.getCourseId());

            // Assume CourseLevel is an enum where the ordinal value indicates the level (higher ordinal, higher level)
            boolean hasCompletedPrerequisite = false;
            for(SheetMark mark : marks) {
                if( mark.getCourseLevel() == requiredLevel && 
                mark.getSheetMarkStatus() == SheetMarkStatus.Completed_in_pass) {
                    hasCompletedPrerequisite = true;
                    break;
                }
            }

            if (!hasCompletedPrerequisite) {
                return "Student has not completed the necessary prerequisites";
            }
        }

        Set<CourseClass> classOfStudent= courseClassRepository.findOverlappingClassesForStudent(student.getId(), CourseClassStatus.inPlanned, courseClass.getDayOfWeek(), courseClass.getStartPeriod(), courseClass.getEndPeriod());
        if(classOfStudent.size() != 0 ) {
            return "Overlapping class for this student";
        }

        // Register the student
        courseClass.addStudentIds(student);
        courseClassRepository.save(courseClass);

        return "Student registered successfully";
    }

    // Cancel Regist method for regist to Course Class
    public String cancekRegisterStudentToClass(Long classId, Long studentId) {
        CourseClass courseClass = courseClassRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Class not found"));

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student course not found"));
            
        Set<Long> studentIds= courseClass.getStudentIds();
        if(!studentIds.contains(studentId)) throw new RuntimeException("Student with id "+ student.getId() + " is not belong to class with id " + courseClass.getId() + " for course with id " + courseClass.getCourseId()+"!!!");


        Optional<SheetMark> sheetMark = sheetMarkRepository.findByCourseIdAndCourseLevelAndStudentIdAndCourseClassId(courseClass.getCourseId(), courseClass.getCourseLevel(), studentId, courseClass.getId());
        if(sheetMark.isPresent()) {
            sheetMarkRepository.delete(sheetMark.get());
        }

        studentIds.remove(studentId);
        courseClass.setStudentIds(studentIds);
        courseClassRepository.save(courseClass);

        return "Student cancel class successfully";
    }
}

    
    

