package com.esliceu.forumapirest.Services;

import com.esliceu.forumapirest.DTOs.ProfileDTO;
import com.esliceu.forumapirest.Forms.RegisterForm;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        /*SHA512 Encrypt*/

        user.setPassword(getSHA512(newuser.getPassword()));
        user.setRole(newuser.getRole());
        user.setRootPermissions("categories:write"+","+"categories:delete");


        userRepo.save(user);
    }


    public boolean checkUser(String email, String password) {
    return userRepo.existsByEmailAndPassword(email,getSHA512(password));

    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }


    public String getSHA512(String password){
        String generatedPassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void changePassword(User u, String newPassword) {
        userRepo.updatePassword(u.getId(),getSHA512(newPassword));
    }

    public void updateUser(long id, ProfileDTO profiledto) {
        userRepo.updateUser(id,profiledto.getName(),profiledto.getEmail());
    }
}
