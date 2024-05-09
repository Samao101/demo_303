package com.example.demo.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.DTOs.AdminDto;
import com.example.demo.models.DTOs.converter.AdminDtoConverter;
import com.example.demo.models.DTOs.request.Admin.CreateAdminRequest;
import com.example.demo.models.DTOs.request.Admin.UpdateAdminRequest;
import com.example.demo.reponse.ResponseObject;
import com.example.demo.service.AdminService;

@RestController
public class AdminController {
    private final AdminService adminService;
    private final AdminDtoConverter adminDtoConverter;

    public AdminController( AdminService adminService,
                            AdminDtoConverter adminDtoConverter) {
        this.adminService = adminService;
        this.adminDtoConverter = adminDtoConverter;
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllAdmins() {
        Set<AdminDto> admins = adminDtoConverter.convert(adminService.getAllAdmins());
        return ResponseEntity.ok(new ResponseObject("success", "All admins fetched successfully", admins));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
            .map(admin -> ResponseEntity.ok(new ResponseObject("success", "Admin fetched successfully", admin)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> getAdminByName(@RequestParam String name) {
        Set<AdminDto> admins = adminDtoConverter.convert(adminService.getAdminByName(name));
        return ResponseEntity.ok(new ResponseObject("success", "Admins fetched successfully", admins));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createAdmin(@RequestBody CreateAdminRequest adminRequest) {
        try {
            AdminDto admin = adminDtoConverter.convert(adminService.createAdmin(adminRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Admin created successfully", admin));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAdmin(@PathVariable Long id, @RequestBody UpdateAdminRequest adminRequest) {
        try {
            adminRequest.setId(id);
            AdminDto updatedAdmin = adminDtoConverter.convert(adminService.updateAdmin(adminRequest));
            return ResponseEntity.ok(new ResponseObject("success", "Admin updated successfully", updatedAdmin));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok(new ResponseObject("success", "Admin deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", e.getMessage(), null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseObject> deleteAllAdmins() {
        adminService.deleteAllStudent();
        return ResponseEntity.ok(new ResponseObject("success", "All admins deleted successfully", null));
    }
}
