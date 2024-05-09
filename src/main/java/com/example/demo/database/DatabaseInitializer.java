package com.example.demo.database;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.enums.Role;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {
    private final UserRepository userRepository;

    public DatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @PostConstruct
    public void init() {
        // Kiểm tra xem tài khoản admin đã tồn tại chưa
        Optional<User> admin = userRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            // Tạo tài khoản admin mới nếu chưa tồn tại
            User newUser = new User();
            newUser.setUsername("admin");
            newUser.setPassword("password"); // mã hóa mật khẩu
            newUser.setRole(Role.ADMIN);
            userRepository.save(newUser);
        }
    }
}