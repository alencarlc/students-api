package com.schoology.app.students.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.schoology.students.service.AppUserService;
import com.schoology.students.service.AuthenticationService;
import com.schoology.students.util.JwtUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationServiceUnitTests.class)
@ActiveProfiles("test")
public class AuthenticationServiceUnitTests {
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private JwtUtil jwtUtil;
	
	@Mock
	private AppUserService userService;
	
	@InjectMocks
	private AuthenticationService service;
	
	@Test
	public void testAuthenticate() {
		UsernamePasswordAuthenticationToken authUser = new UsernamePasswordAuthenticationToken("manager", "password");
		when(authenticationManager.authenticate(any())).thenReturn(authUser);
		Authentication authRetuned = this.service.authenticate("manager", "password");
		this.service.authenticate("manager", "password");
		assertNotNull(authRetuned);
		String returnedUsername = (String) authRetuned.getPrincipal();
		String loggedUsername = (String) authUser.getPrincipal();
		assertEquals(loggedUsername, returnedUsername);
	}
	
	@Test(expected = BadCredentialsException.class)
	public void testAuthenticateThrowsBadCredentialsException() {
		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad credentials"));
		this.service.authenticate("manager", "password");
	}
	
	@Test
	public void testGenerateToken() {
		UserDetails testUser = new User("manager", "password", new ArrayList<>());
		when(userService.loadUserByUsername(anyString())).thenReturn(testUser);
		final String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiZXhwIjoxNTgxMjg1NjI1LCJpYXQiOjE1ODEyNjc2MjV9.jKV8kx_qmnV9VyGmI7SSM-SFbGOJEhmDDHQ35JHNYt9pFz2W4qudfcXV4ZDnyhztQMBIr0n4DxJvrca9A2MVYw";
		when(jwtUtil.generateToken(any())).thenReturn(jwt);
		String returnedJwt = this.service.generateToken(testUser);
		assertEquals(jwt, returnedJwt);
	}
	
}
