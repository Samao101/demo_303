package com.example.demo.Repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT cc FROM User cc")
    Set<User> findAlls();

    Optional<User> findByUsername(String username);
}
