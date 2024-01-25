package com.example.solutionchallenge.app.user.repository;

import com.example.solutionchallenge.app.user.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByFirebaseUid(String firebaseUid);

}
