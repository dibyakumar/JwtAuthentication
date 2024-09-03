package com.auth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.service.CustomUserDetailsService;
import com.auth.service.JwtConfigService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component						   // for each request the OncePerRequestFilter ensures filter-method will be invoked only once 
public class JwtAuthFilter extends OncePerRequestFilter{

	@Autowired
	private JwtConfigService jwtService;
	
	@Autowired 
	private  CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		/**
		 * When implementing JWT Authentication, checking for the presence of the Bearer keyword in the Authorization header 
		 * is necessary to ensure that the server correctly identifies and processes JWT tokens. 
		 * Since the server might receive various types of tokens or credentials in the Authorization header, 
		 * including API keys, OAuth 2.0 tokens, or other custom tokens, specifying the Bearer scheme helps 
		 * the server distinguish JWT tokens specifically intended for authentication.
		 */
		
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String username;								// tokens starts with bearer 
		if(authHeader == null  ||  !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
										// as 'bearer ' size is 7
		jwtToken = authHeader.substring(7);
		username = jwtService.extractUsername(jwtToken);
								// check whether the user is authenticated or not 
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userdetails = this.userDetailsService.loadUserByUsername(username);
			// if the token is valid then save the user credentials in security context holder
			if(jwtService.isTokenValid(jwtToken, userdetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userdetails, null, userdetails.getAuthorities()
						);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
