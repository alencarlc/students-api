package com.schoology.students.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.schoology.students.service.AuthenticationService;
import com.schoology.students.web.constraints.ExceptionMessages;
import com.schoology.students.web.dto.request.AuthenticationRequest;
import com.schoology.students.web.dto.response.AuthenticationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = AuthenticationRestController.AUTH_API_TAG, description = "JWT Authentication API")
public class AuthenticationRestController {
	
	@Autowired
	private AuthenticationService authService;
	
	protected static final String AUTH_API_TAG = "Authentication";
	
	@Operation(summary = "Token creation", description = "Authenticate an username and password and return a JWT, test using username: manager password: password", tags = { AuthenticationRestController.AUTH_API_TAG })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credentials accepted and JWT returned", 
                content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))})
	
	@RequestMapping(value = "/${api.version.v1}/authentication", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> createAuthenticationToken(@Parameter(description="Username and password to get a JWT.", required=true) @RequestBody AuthenticationRequest tokenRequest){
		try {
			Authentication auth = this.authService.authenticate(tokenRequest.getUsername(), tokenRequest.getPassword());
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			return this.createResponseEntityContaingToken(userDetails);
		}catch(BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(ExceptionMessages.BAD_CREDENTIALS_EXCEPTION, tokenRequest.getUsername(), tokenRequest.getPassword()), e);
		}
	}

	@Operation(summary = "Token refresh", description = "Evaluate the 'Authorization' header and if there is a valid JWT, a new one will be returned.", tags = { AuthenticationRestController.AUTH_API_TAG },
			security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New JWT returned", 
                content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))) })	
	@RequestMapping(value = "/${api.version.v1}/authentication", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> refreshAuthenticationToken(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.createResponseEntityContaingToken(userDetails);
	}
	
	private ResponseEntity<AuthenticationResponse> createResponseEntityContaingToken(UserDetails userDetails) {
		return ResponseEntity.ok(AuthenticationResponse.builder().token(this.authService.generateToken(userDetails)).build());
	}
}
