package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.entity.AuthenticationRequest;
import com.auth.entity.AuthenticationResponse;
import com.auth.entity.JwtUser;
import com.auth.entity.RegisterRequest;
import com.auth.repository.JwtUserRepo;

@Service
public class AuthenticationService {

	@Autowired
	private JwtUserRepo userRepo;
	
	@Autowired
	private JwtConfigService jwtService;
	
	@Autowired
	private UserDetailsService uservice;
	
	@Autowired
	private PasswordEncoder paswordEncoder;
	
	@Autowired
	private AuthenticationManager authmanager;
	public AuthenticationResponse register(RegisterRequest request) {
		var user = JwtUser.builder()
				.email(request.getEmail())
				.password(paswordEncoder.encode(request.getPassword()))
				.role(request.getRole())
				.build();
		userRepo.save(user);
		var jwtToken = jwtService.generateToken(uservice.loadUserByUsername(user.getEmail()));
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authmanager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
				);
		var user = userRepo.findById(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(uservice.loadUserByUsername(user.getEmail()));
		return AuthenticationResponse.builder().token(jwtToken).build();
	}
	
	
}
