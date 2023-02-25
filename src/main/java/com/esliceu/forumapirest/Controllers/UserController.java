package com.esliceu.forumapirest.Controllers;

import com.esliceu.forumapirest.DTOs.PasswprdDTO;
import com.esliceu.forumapirest.DTOs.ProfileDTO;
import com.esliceu.forumapirest.Forms.LoginForm;
import com.esliceu.forumapirest.DTOs.LoginDTO;
import com.esliceu.forumapirest.Forms.RegisterForm;
import com.esliceu.forumapirest.Models.Category;
import com.esliceu.forumapirest.Models.User;
import com.esliceu.forumapirest.Services.CategoryService;
import com.esliceu.forumapirest.Services.TokenService;
import com.esliceu.forumapirest.Services.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    CategoryService categoryService;


    @PostMapping("/register")
    @CrossOrigin

    public Map<String, String> register(@RequestBody RegisterForm registerForm, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        System.out.println(registerForm);
        map.put("message", "User registered");
        if (userService.existsUser(registerForm.getEmail())) {
            map.put("message", "User already exists");
            response.setStatus(409);
        } else {
            userService.registerUser(registerForm);
        }
        return map;
    }

    @PostMapping("/login")
    @CrossOrigin
    public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        if (userService.checkUser(loginForm.getEmail(), loginForm.getPassword())) {
            User u = userService.getUserByEmail(loginForm.getEmail());

            LoginDTO lh = new LoginDTO();
            lh.setRole(u.getRole());
            lh.set_id((int) u.getId());
            lh.setEmail(u.getEmail());
            lh.setName(u.getName());
            lh.set__v(0);
            lh.setAvatarUrl("");
            lh.setId((int)u.getId());
            Map <String, Object> map2 = new HashMap<>();
            String[] perms = {"categories:write","categories:delete"};
            map2.put("root",perms);

            map2.put("categories",categoryService.getAllCategories());
            lh.setPermissions(map2);




            String token = tokenService.newToken(loginForm.getEmail());
            map.put("user", lh);
            map.put("token",token);
            response.setStatus(200);
        } else {
            map.put("message", "Username or password incorrect");
            response.setStatus(401);
        }

        return map;
    }

    @GetMapping("/getprofile")
    @CrossOrigin
    public Object getProfile(HttpServletRequest request, HttpServletResponse response, @RequestHeader("Authorization") String token) {


        Map<String, Object> map = new HashMap<>();
        String email = tokenService.getEmailFromToken(token.replace("Bearer ", ""));
        User u = userService.getUserByEmail(email);
        LoginDTO lh = new LoginDTO();
        lh.setRole(u.getRole());
        lh.set_id((int) u.getId());
        lh.setEmail(u.getEmail());
        lh.setName(u.getName());
        lh.set__v(0);
        lh.setAvatarUrl("");
        lh.setId((int)u.getId());
            Map <String, Object> map2 = new HashMap<>();
            String[] perms = {"categories:write","categories:delete","own_topics:write","own_topics:delete","own_replies:write","own_replies:delete"};
            map2.put("root",perms);

            List<Category> categories = categoryService.getAllCategories();
            for(Category c: categories) {
                Map<String, Object> map3 = new HashMap<>();
                String[] perms2 = {"categories_topics:write","categories_topics:delete","categories_replies:write","categories_replies:delete"};
                map3.put(c.getTitle(),perms2);
                map2.put("categories",map3);
            }





        lh.setPermissions(map2);

        map.put("role", lh.getRole());
        map.put("_id", lh.getId());
        map.put("email", lh.getEmail());
        map.put("name", lh.getName());
        map.put("__v", lh.get__v());
        map.put("avatarUrl", lh.getAvatarUrl());
        map.put("id", lh.getId());
        map.put("permissions", lh.getPermissions());
        map.put("iat", tokenService.getIatFromToken(token.replace("Bearer ", "")));
        /*pAYLOAD*/

        Gson gson = new Gson();
        String json = gson.toJson(map);
        System.out.println(json);
        return map;
    }

    @PutMapping("/profile/password")
    @CrossOrigin
    public Map<String, Object> changePassword(HttpServletRequest request, HttpServletResponse response, @RequestHeader("Authorization") String token, @RequestBody PasswprdDTO passworddto) {
        User u = userService.getUserByEmail(tokenService.getEmailFromToken(token.replace("Bearer ", "")));
        boolean checkOldPasswd = userService.checkUser(u.getEmail(), passworddto.getCurrentPassword());
        Map<String, Object> map = new HashMap<>();
        if (passworddto.getNewPassword().equals(passworddto.getCurrentPassword()) && checkOldPasswd) {

            map.put("message", "Your new password is the same as the old one");
            response.setStatus(400);
        }else if (checkOldPasswd) {
            userService.changePassword(u, passworddto.getNewPassword());
            map.put("message", true);
        } else{
            map.put("message", "Your current password is incorrect");
            response.setStatus(400);
        }
        return map;
    }

    @PutMapping("/profile")
    @CrossOrigin
    public Map<String, Object> changeProfile(HttpServletRequest request, HttpServletResponse response, @RequestHeader("Authorization") String token, @RequestBody ProfileDTO profiledto) {

    Map<String, Object> map =  new HashMap<>();
        User u = userService.getUserByEmail(tokenService.getEmailFromToken(token.replace("Bearer ", "")));
        userService.updateUser(u.getId(), profiledto);
        Map<String, Object> map2 = new HashMap<>();
        map.put("message", true);
        map2.put("avatarUrl", "");
        map2.put("email", profiledto.getEmail());
        map2.put("id", u.getId());
        map2.put("name", profiledto.getName());
        map2.put("role", u.getRole());
        map2.put("__v", 0);
        map2.put("_id", u.getId());
        Map <String, Object> map3 = new HashMap<>();
        String[] perms = {"categories:write","categories:delete","own_topics:write","own_topics:delete","own_replies:write","own_replies:delete"};
        map3.put("root",perms);
        map3.put("categories",categoryService.getAllCategories());
        map2.put("permissions",map3);



        map.put("user", map2);
        return map;

    }


}

