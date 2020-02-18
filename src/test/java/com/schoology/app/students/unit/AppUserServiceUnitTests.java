package com.schoology.app.students.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.schoology.students.domain.AppUser;
import com.schoology.students.service.AppUserService;
import com.schoology.students.service.repository.AppUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppUserServiceUnitTests.class)
public class AppUserServiceUnitTests {
	
	@Mock
	private AppUserRepository repository;
	
	@InjectMocks
	private AppUserService service;
	
	@Test
	public void testFindByUsername() {
		when(repository.findByUsername(anyString())).thenReturn(AppUser.builder().username("manager").password("password").build());
		AppUser userFound = this.service.findAppUserByUsername("manager");
		assertEquals(userFound.getUsername(), "manager");
		assertEquals(userFound.getPassword(), "password");
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testFindByUsernameThrowsUsernameNotFoundException() {
		when(repository.findByUsername(anyString())).thenReturn(null);
		@SuppressWarnings("unused")
		AppUser userFound = this.service.findAppUserByUsername("manager");
	}
	
	@Test
	public void testLoadUserByUsername() {
		AppUserService spiedService = spy(AppUserService.class);
		doReturn(AppUser.builder().username("manager").password("password").build()).when(spiedService).findAppUserByUsername(anyString());
		UserDetails userDetails = spiedService.loadUserByUsername("manager");
		assertEquals(userDetails.getUsername(), "manager");
		assertEquals(userDetails.getPassword(), "password");
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameThrowsUsernameNotFoundException() {
		when(service.findAppUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));
		@SuppressWarnings("unused")
		UserDetails userDetails = service.loadUserByUsername("manager");
	}
}

