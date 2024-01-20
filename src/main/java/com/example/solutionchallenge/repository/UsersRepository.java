package com.example.solutionchallenge.repository;

import com.example.solutionchallenge.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByFirebaseUid(String firebaseUid);
}
