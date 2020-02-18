package com.schoology.students.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.schoology.students.constraints.PaginationConstraints;
import com.schoology.students.domain.Student;
import com.schoology.students.dto.PaginatedEntityResults;
import com.schoology.students.service.repository.StudentRepository;
import com.schoology.students.util.StringUtil;

@Service
public class StudentService {
	
	@Autowired
	private PaginationConstraints paginationConstraints;
	
	@Autowired
	private StudentRepository repository;
	
	public PaginatedEntityResults findByNameLike(String name, Integer limit, Integer page) {
		//in case of limit is empty or invalid the api use the default or max limit defined in properties source
		if (limit == null || limit < 0 ) {
			limit = this.paginationConstraints.getDefaultLimit();
		}else if(limit > this.paginationConstraints.getMaxLimit()) {
			limit = this.paginationConstraints.getMaxLimit();
		}
		
		//in case of invalid or empty offset the api use the default offset defined in properties source
		if (page == null || page < 0 ) {
			page = paginationConstraints.getDefaultPage();
		}
		
		if (name == null) {
			name = StringUtil.EMPTY_STRING;
		}
		
		final List<Student> studentsFound = this.repository.findByNameContainingOrderByIdDesc(name, PageRequest.of(page, limit));
		final Long count = this.countByNameContaining(name);
		
		//PaginatedEntityResults contains the results and the total entries in the database, for pagination purposes
		return PaginatedEntityResults.builder().results(studentsFound)
											   .total(count)
											   .build();
	}
	
	public Long countByNameContaining(String name) {
		return this.repository.countByNameContaining(name);
	}
	

}
