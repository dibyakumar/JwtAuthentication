package com.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.entity.CustomUserDetails;
import com.auth.entity.JwtUser;
import com.auth.repository.JwtUserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private JwtUserRepo userrepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<JwtUser> findById = userrepo.findById(username);
		if(findById.isEmpty()) throw new RuntimeException("User Not Found");
		return new CustomUserDetails(findById.get());
	}
	

}
