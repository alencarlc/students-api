package com.schoology.students.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.schoology.students.domain.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{
	
	AppUser findByUsername(String username);

}
