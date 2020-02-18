package com.schoology.students.constraints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
@Getter @NoArgsConstructor
public class PaginationConstraints {
	
	@Value("${pagination.default.page}")
	private Integer defaultPage;
	
	@Value("${pagination.default.limit}")
	private Integer defaultLimit;
	
	@Value("${pagination.default.maxLimit}")
	private Integer maxLimit;

}
