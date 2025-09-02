package com.dinhnguyendev.springsecuritysection1;

import java.lang.reflect.InvocationTargetException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class Springsecuritysection1Application {

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

		SpringApplication.run(Springsecuritysection1Application.class, args);
	}

}
