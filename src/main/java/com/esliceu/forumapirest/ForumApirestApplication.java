package com.esliceu.forumapirest;

import com.esliceu.forumapirest.Interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ForumApirestApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ForumApirestApplication.class, args);
	}

	@Autowired
	TokenInterceptor tokenInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenInterceptor).addPathPatterns("/getprofile").addPathPatterns("/c/**");}
}
