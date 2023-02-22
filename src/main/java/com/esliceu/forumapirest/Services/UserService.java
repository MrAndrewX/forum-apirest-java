package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.Forms.RegisterForm;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public boolean existsUser(String email) {
        return userRepo.existsByEmail(email);

    }

    public void registerUser(RegisterForm newuser) {
        User user = new User();
        user.setEmail(newuser.getEmail());
        user.setName(newuser.getName());
        user.setPassword(newuser.getPassword());
        user.setRole(newuser.getRole());
        user.setRootPermissions("categories:write"+","+"categories:delete");


        userRepo.save(user);
    }


    public boolean checkUser(String email, String password) {
    return userRepo.existsByEmailAndPassword(email,password);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
