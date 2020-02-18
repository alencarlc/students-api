package com.schoology.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.schoology.students.util.JwtUtil;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;

	public Authentication authenticate(String username, String password) throws BadCredentialsException {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
	
	public String generateToken(UserDetails userDetails) {
		return jwtUtil.generateToken(userDetails);
	}

}
