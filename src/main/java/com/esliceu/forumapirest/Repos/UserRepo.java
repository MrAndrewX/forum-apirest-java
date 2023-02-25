package com.esliceu.forumapirest.Repos;

import com.esliceu.forumapirest.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Transactional
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);
    User findByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void updatePassword(@Param("id") long id, @Param("password") String password);

    @Modifying
    @Query("update User u set u.name = :name, u.email = :email where u.id = :id")
    void updateUser(@Param("id") long id,@Param("name") String name,@Param("email") String email);
}
