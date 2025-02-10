package com.example.demoo.repo;

import com.example.demoo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username and password = :password", nativeQuery = true)
    Optional<User> getByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
