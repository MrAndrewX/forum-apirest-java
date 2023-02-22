package com.esliceu.forumapirest.Interceptors;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.esliceu.forumapirest.Services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {



    @Autowired
    TokenService tokenService;
//TODO ARREGLAR USUARIOS SIN TOKEN PUEDAN VISUALIZAR
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS")){
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User or password incorrect");
        }
        try {
            String token =  authHeader.replace("Bearer ","");
            //Validate token
            String username = tokenService.getUser(token);
            request.setAttribute("username",username);
            return true;
        }catch (SignatureVerificationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User or password incorrect");
        }
    }
}
