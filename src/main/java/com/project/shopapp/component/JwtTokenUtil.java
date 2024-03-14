package com.project.shopapp.component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
	@Value("${jwt.expiration}")
	private int expiration;

	@Value("${jwt.secretKey}")
	private String secretKey;

	public String generateToken(com.project.shopapp.models.User user) throws Exception {
		// properties => claims
		Map<String, Object> claims = new HashMap<>();
		//this.generateSecrectKey();
		claims.put("phoneNumber", user.getPhoneNumber());
		try {
			String token = Jwts.builder().setClaims(claims).setSubject(user.getPhoneNumber())
					.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
			return token;
		} catch (Exception e) {
			// TODO: handle exception
			throw new InvalidParameterException("Cannot create jwt token, error: " + e.getMessage());
		}
	}

	private Key getSignKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey); 
		// Decoders.BASE64.decode("txAAjqYcAsDcTp/bPq0Gms1GEvaaLp3zcTUiUwRkzJY=")
		return Keys.hmacShaKeyFor(bytes);
		//Keys.hmacShaKeyFor("txAAjqYcAsDcTp/bPq0Gms1GEvaaLp3zcTUiUwRkzJY=")
	}

	private String generateSecrectKey() {
		SecureRandom random = new SecureRandom();
		byte[] keyBytes = new byte[32];
		random.nextBytes(keyBytes);
		String secretKey = Encoders.BASE64.encode(keyBytes);
		return secretKey;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// check expiration
	public boolean isTokenExpired(String token) {
		Date expirationDate = this.extractClaim(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}
	
	public String extractPhoneNumber(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		String phoneNumber = extractPhoneNumber(token);
		return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	
}
