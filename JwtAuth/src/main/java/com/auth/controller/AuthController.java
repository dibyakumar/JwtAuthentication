package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.entity.AuthenticationRequest;
import com.auth.entity.AuthenticationResponse;
import com.auth.entity.RegisterRequest;
import com.auth.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private  AuthenticationService authservice;
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request){
		return ResponseEntity.ok(authservice.register(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationRequest request){
		return ResponseEntity.ok(authservice.authenticate(request));
	}
	

}
