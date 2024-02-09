package com.example.solutionchallenge.app.user.repository;

import com.example.solutionchallenge.app.user.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
}

