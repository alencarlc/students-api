package com.schoology.app.students.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.schoology.students.Application;
import com.schoology.students.util.JwtUtil;

import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationRestControllerIntegrationTests {
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Value("${api.version.v1}")
    private String apiV1path;
    
    @LocalServerPort
    private int port;
    	
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private FilterChainProxy filterChainProxy;
    
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(filterChainProxy).build();
    }
    
    @Test
    public void testCreateAuthenticationToken() throws Exception {
        this.mockMvc.perform(post(createURLWithPort(apiV1path+"/authentication"))
        			.content(this.createValidCredentialsUserJson()).contentType(MediaType.APPLICATION_JSON))
        			.andExpect(status().is2xxSuccessful());
    }
    
    @Test
    public void testCreateAuthenticationTokenBadCredentials() throws Exception {
        this.mockMvc.perform(post(createURLWithPort(apiV1path+"/authentication"))
        			.content(this.createInvalidCredentialsUserJson()).contentType(MediaType.APPLICATION_JSON))
        			.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testRefreshAuthenticationToken() throws Exception {
    	UserDetails testUser = new User("manager", "password2", new ArrayList<>());
    	String jwt = this.jwtUtil.generateToken(testUser);
    	HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(jwt);
        this.mockMvc.perform(get(createURLWithPort(apiV1path+"/authentication"))
        			.headers(requestHeaders)
        			.contentType(MediaType.APPLICATION_JSON))
        			.andExpect(status().is2xxSuccessful());
    }
    
    @Test
    public void testRefreshAuthenticationTokenUnauthorized() throws Exception {
        this.mockMvc.perform(get(createURLWithPort(apiV1path+"/authentication"))
        			.contentType(MediaType.APPLICATION_JSON))
        			.andExpect(status().is4xxClientError());
    }
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/" +uri;
    }

	private String createValidCredentialsUserJson() {
		JSONObject json = new JSONObject();
    	json.put("username", "manager");
    	json.put("password", "password");
    	return json.toString();
	}   
    
	private String createInvalidCredentialsUserJson() {
		JSONObject json = new JSONObject();
    	json.put("username", "manager2");
    	json.put("password", "password");
    	return json.toString();
	}   
    
}
