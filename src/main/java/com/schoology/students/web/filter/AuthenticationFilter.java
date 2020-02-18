package com.schoology.students.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.schoology.students.service.AppUserService;
import com.schoology.students.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private static final String JWT_PREFIX = "Bearer ";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = null;
		String jwt = null;
		
		// Removing Bearer word and getting only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith(AuthenticationFilter.JWT_PREFIX)) {
			jwt = requestTokenHeader.substring(7);
			try {
				username = jwtUtil.getUsernameFromToken(jwt);
			} catch (IllegalArgumentException e) {
				logger.warn("Invalid JWT");
			} catch (ExpiredJwtException e) {
				logger.warn("Expired token");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer");
		}
		
		// Validating token
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.appUserService.loadUserByUsername(username);
			// if token is valid configure Spring Security and, manually setting the authentication
			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// we set that current user in the Spring Security context
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
