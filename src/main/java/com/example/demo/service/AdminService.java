package com.example.demo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.Repositories.AdminRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.request.Admin.CreateAdminRequest;
import com.example.demo.models.DTOs.request.Admin.UpdateAdminRequest;
import com.example.demo.models.entity.Admin;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.enums.Role;

@Service
public class AdminService {
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final UserService userService;

    public AdminService(AdminRepository adminRepository,
                        UserService userService,
                        StudentRepository studentRepository,
                        TeacherRepository teacherRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userService = userService;
    }

    public Set<Admin> getAllAdmins() {
        return new HashSet<>(adminRepository.findAll());
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Set<Admin> getAdminByName(String name) {
        return adminRepository.findByName(name);
    }



    public Admin createAdmin(CreateAdminRequest adminRequest){
        if (adminRepository.existsByCitizenIdentification(adminRequest.getCitizenIdentification()) || 
            teacherRepository.existsByCitizenIdentification(adminRequest.getCitizenIdentification()) ||
            studentRepository.existsByCitizenIdentification(adminRequest.getCitizenIdentification())) {
            throw new IllegalArgumentException("This citizen identification already exists.");
        }
        
        if (adminRepository.existsByEmail(adminRequest.getEmail()) ||
            teacherRepository.existsByEmail(adminRequest.getEmail()) ||
            studentRepository.existsByEmail(adminRequest.getEmail())) {
            throw new IllegalArgumentException("This email already exists.");
        }

        if (adminRepository.existsByPhoneNumber(adminRequest.getPhoneNumber()) ||
            teacherRepository.existsByPhoneNumber(adminRequest.getPhoneNumber()) ||
            studentRepository.existsByPhoneNumber(adminRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("This phone number already exists.");
        }

        Admin admin = new Admin();

        admin.setName(adminRequest.getName());
        admin.setAge(adminRequest.getAge());
        admin.setGender(adminRequest.getGender());
        admin.setDateOfBirth(adminRequest.getDateOfBirth());
        admin.setPlaceOfBirth(adminRequest.getPlaceOfBirth());
        admin.setCitizenIdentification(adminRequest.getCitizenIdentification());
        admin.setEmail(adminRequest.getEmail());
        admin.setPhoneNumber(adminRequest.getPhoneNumber());

        Admin savedAdmin = adminRepository.save(admin);

        try {
            User adminUser = userService.createUser("admin_" + savedAdmin.getId(), Role.ADMIN);
            savedAdmin.setUser(adminUser);
        } catch (Exception e) {
            adminRepository.delete(savedAdmin);
            throw new IllegalArgumentException("Could not create account: " + e.getMessage());
        }
       
        
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(UpdateAdminRequest updatedAdmin) {
        Long id= updatedAdmin.getId();
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + id));

        adminRepository.findByCitizenIdentification(updatedAdmin.getCitizenIdentification())
        .filter(existingAdmin -> (!existingAdmin.getId().equals(id)) || 
                                    teacherRepository.existsByCitizenIdentification(updatedAdmin.getCitizenIdentification()))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Citizen Identification is already in use.");
        });

        adminRepository.findByEmail(updatedAdmin.getEmail())
        .filter(existingAdmin -> (!existingAdmin.getId().equals(id) ||
                                    teacherRepository.existsByEmail(updatedAdmin.getEmail())))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Email is already in use.");
        });

        adminRepository.findByPhoneNumber(updatedAdmin.getPhoneNumber())
        .filter(existingAdmin -> (!existingAdmin.getId().equals(id) ||
                                    teacherRepository.existsByPhoneNumber(updatedAdmin.getPhoneNumber())))
        .ifPresent(s -> {
            throw new IllegalArgumentException("Phone number is already in use.");
        });
        
        admin.setName(updatedAdmin.getName());
        admin.setAge(updatedAdmin.getAge());
        admin.setGender(updatedAdmin.getGender());
        admin.setDateOfBirth(updatedAdmin.getDateOfBirth());
        admin.setPlaceOfBirth(updatedAdmin.getPlaceOfBirth());
        admin.setCitizenIdentification(updatedAdmin.getCitizenIdentification());
        admin.setEmail(updatedAdmin.getEmail());
        admin.setPhoneNumber(updatedAdmin.getPhoneNumber());

        return adminRepository.save(admin);
    }

    // All delete method for delete request
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id " + adminId));
    
        adminRepository.delete(admin);
    }

    public void deleteAllStudent() {
        adminRepository.deleteAll();
    }

}
