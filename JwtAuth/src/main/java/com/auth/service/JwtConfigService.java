package com.auth.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtConfigService {
	
	// secret key which only server have ,  server use this secret key to check the token is 
		// valid or not .
		private static final String secret = "Ti8rDVaXBmB8hUOFU3OVAklmA8HG5lMKPv1q5Jy1R7WPsD38m8XAv9QZWLgGS7/j"; // base64
		
		
		// check token is valid or not
		public boolean isTokenValid(String token, UserDetails userDetail) {
			final String username = extractUsername(token);
			return username.equals(userDetail.getUsername()) && !isTokenExpired(token);
		}
	
	private boolean isTokenExpired(String token) {
			return extractExpiration(token).before(new Date());
		}
	

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// generate token without payaload
		public String generateToken(UserDetails userDetails) {
			return generateToken(new HashMap<>() , userDetails);
		}
		
		// generate token with payloads 
		public String generateToken(Map<String,Object> extraClaims,
				UserDetails userDetails) {
			return Jwts
					.builder()
					.setClaims(extraClaims)
					.setSubject(userDetails.getUsername())
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
					.signWith(getSigninKey(),SignatureAlgorithm.HS256)
					.compact();
		}
		
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
		// get a specific data from payload 												// <input,result>
		public <T> T extractClaim(String token,Function<Claims,T> claimsResolver) {
			final Claims claim = extractAllClaims(token);
			return claimsResolver.apply(claim);
		}
	
		// get all the payload data 
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.setSigningKey(getSigninKey())
				.build().parseClaimsJws(token)
				.getBody();
	}

	private Key getSigninKey() {
			byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
}
