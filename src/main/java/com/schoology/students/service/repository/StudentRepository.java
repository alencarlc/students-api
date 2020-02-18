package com.schoology.students.service.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.schoology.students.domain.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long>{
	
	List<Student> findByNameContainingOrderByIdDesc(String name, Pageable pageConfig);
	Long countByNameContaining(String name);

}
