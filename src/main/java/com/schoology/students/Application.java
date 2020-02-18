package com.schoology.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@SpringBootApplication
@SecuritySchemes(value = @SecurityScheme(name = "BearerAuth",type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT"))
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
