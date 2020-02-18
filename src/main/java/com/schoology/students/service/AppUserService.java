package com.schoology.students.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.schoology.students.domain.AppUser;
import com.schoology.students.service.repository.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {
	
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = this.findAppUserByUsername(username);
		
		//This api is not worried about any authorization, only authentication, so Authorities is an empty array
		return new User(user.getUsername(), user.getPassword(),	new ArrayList<>());
	}
	
	public AppUser findAppUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user =  this.appUserRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return user;
	}

}
