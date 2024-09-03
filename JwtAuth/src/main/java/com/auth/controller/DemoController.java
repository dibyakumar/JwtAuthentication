package com.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DemoController {
	
	@GetMapping("/get")
	public ResponseEntity<String> check(){
		return ResponseEntity.ok("HII You are Secured !!");
	}
}
