package com.example.solutionchallenge.app.user.repository;

import com.example.solutionchallenge.app.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByFirebaseUid(String firebaseUid);
}
