package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
