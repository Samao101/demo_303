package com.example.demo.service;

import java.util.Set;

// import java.util.UUID;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Repositories.AdminRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.models.entity.Teacher;
import com.example.demo.models.entity.Student;
import com.example.demo.models.entity.Admin;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.enums.Role;
import com.example.demo.models.DTOs.converter.AdminDtoConverter;
import com.example.demo.models.DTOs.converter.StudentDtoConverter;
import com.example.demo.models.DTOs.converter.TeacherDtoConverter;


@Service
public class UserService {

    private final StudentDtoConverter studentDtoConverter;
    private final TeacherDtoConverter teacherDtoConverter;
    private final AdminDtoConverter adminDtoConverter;

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public UserService( UserRepository userRepository,
                        TeacherRepository teacherRepository,
                        StudentRepository studentRepository,
                        AdminRepository adminRepository,
                        StudentDtoConverter studentDtoConverter,
                        TeacherDtoConverter teacherDtoConverter,
                        AdminDtoConverter adminDtoConverter) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;

        this.adminDtoConverter = adminDtoConverter;
        this.studentDtoConverter = studentDtoConverter;
        this.teacherDtoConverter = teacherDtoConverter;
    }

    public Set<User> getAllUser() {
        return userRepository.findAlls();
    }

    // Tạo tài khoản với mật khẩu tạm thời và gửi thông tin cho người dùng
    public User createUser(String username, Role role) {
        // String temporaryPassword = generateTemporaryPassword(); // Sinh mật khẩu tạm thời
        // User newUser = new User(username, passwordEncoder.encode(temporaryPassword), Role.STUDENT);

        String temporaryPassword = "1"; // Sinh mật khẩu tạm thời
        User newUser = new User(username, temporaryPassword, role);

        userRepository.save(newUser);

        // mailService.sendEmail(email, "Thông tin đăng nhập tạm thời", 
        //     "Username: " + username + "\nTemporary Password: " + temporaryPassword + 
        //     "\nVui lòng đổi mật khẩu sau khi đăng nhập lần đầu.");

        return newUser;
    }

    // private String generateTemporaryPassword() {
    //     // Tạo mật khẩu ngẫu nhiên
    //     return UUID.randomUUID().toString().substring(0, 8);
    // }

    public Object authenticate(String username, String password, Role role) {
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        switch (role) {
            case STUDENT:
                Student student=  studentRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("No student associated with this user"));
                return studentDtoConverter.convert(student);
            case TEACHER:
                Teacher teacher = teacherRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("No teacher associated with this user"));
                return teacherDtoConverter.convert(teacher);
            case ADMIN:
                Admin admin=  adminRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("No admin associated with this user"));
                return adminDtoConverter.convert(admin);
            default:
                throw new IllegalArgumentException("Invalid role");
        }
    }

    public Boolean changePassword(String username, String password, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        if (password.equals(user.getPassword())) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }

        return false;
    }
}

