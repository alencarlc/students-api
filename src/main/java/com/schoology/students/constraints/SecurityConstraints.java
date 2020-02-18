package com.schoology.students.constraints;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Component
@Getter @NoArgsConstructor
public class SecurityConstraints {
	
	private String[] publicUrls = new String[]{
										     "/h2-console/**",
										     "/v3/api-docs",
								             "/configuration/ui",
								             "/swagger-resources/**",
								             "/configuration/security",
								             "/swagger-ui.html",
								             "/swagger-ui/**",
								             "/webjars/**"};
}
