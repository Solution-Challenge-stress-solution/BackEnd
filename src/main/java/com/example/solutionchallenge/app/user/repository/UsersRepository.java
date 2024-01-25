package com.example.solutionchallenge.app.user.repository;

import com.example.solutionchallenge.app.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {


}
