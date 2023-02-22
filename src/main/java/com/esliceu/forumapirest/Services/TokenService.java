package com.esliceu.forumapirest.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.esliceu.forumapirest.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    @Value("${token.secret}")
    String tokenSecret;
    @Value("${token.expiration}")
    int tokenExpiration;
    @Autowired
    UserService userService;

    public String newToken(String email) {
        Map<String, Object> rootpermisions = new HashMap<>();
        rootpermisions.put("root", new String[]{"categories:write", "categories:delete"});
        Map<String, Object> categories = new HashMap<>();
categories.put("categories", new String[]{""});
        Map<String, Object> map = new HashMap<>();
        map.put("role", userService.getUserByEmail(email).getRole());
        map.put("_id", userService.getUserByEmail(email).getId());
        map.put("email", email);
        map.put("name", userService.getUserByEmail(email).getName());
        map.put("__v", 0);
        map.put("avatarUrl", "");
        map.put("permissions", rootpermisions);
        map.put("id", userService.getUserByEmail(email).getId());
        map.put("iat",new Date(System.currentTimeMillis()));


        String token = JWT.create()
                .withPayload(map)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC512(tokenSecret.getBytes()));
        return token;
    }
    public String getUser(String token) {

        //
        String username =  JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getSubject();


        return username;
    }




    public String getEmailFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getClaim("email")
                .asString();
    }

    public Long getIatFromToken(String bearer) {
        return JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(bearer)
                .getClaim("iat")
                .asLong();
    }
}
