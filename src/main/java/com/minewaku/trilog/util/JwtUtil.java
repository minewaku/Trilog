package com.minewaku.trilog.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;

@Service
public class JwtUtil {
	
    private final static String SECRET_KEY = "943d7a6eb3d52c7a233d81d96f0da76ed964777f7a51c9916a829fea261aa2aa";
    
    private static final long ACCESS_TOKEN_EXPIRATION = 86400000; // 1 day (in miliseconds)
    private static final long REFRESH_TOKEN_EXPIRATION = 2592000000L; // 30 days (in miliseconds)
    
    @Resource(name = "hashRedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

	public String extractIssuer(String token) {
		return extractClaim(token, Claims::getIssuer);
	}
    
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
	public String extractAudience(String token) {
		return extractClaim(token, Claims::getAudience);
	}	

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public Date extractNotBefore(String token) {
    	return extractClaim(token, Claims::getNotBefore);
    }
    
	public Date extractIssuedAt(String token) {
		return extractClaim(token, Claims::getIssuedAt);
	}
	
	public String extractId(String token) {
		return extractClaim(token, Claims::getId);
	}

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    public String generateJwtToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), ACCESS_TOKEN_EXPIRATION, userDetails);
    }
    
//	public Cookie generateRefreshToken() {
//        Cookie refreshToken = new Cookie("refreshToken", UUID.randomUUID().toString());
//        refreshToken.setHttpOnly(true);
//        refreshToken.setPath("api/auth/refrehs");
//        refreshToken.setSecure(true);
//        refreshToken.setMaxAge((int) REFRESH_TOKEN_EXPIRATION);
//        
//        return refreshToken;
//	}
    
    //remember to set httpOnly and secure to true 
    public ResponseCookie generateRefreshToken() {
    	return ResponseCookie.from("refresh_token", UUID.randomUUID().toString())
    						  .httpOnly(false)
    						  .path("/api/v1/auth/refresh")
    						  .secure(false)
    						  .maxAge(REFRESH_TOKEN_EXPIRATION / 1000) //maxAge in seconds
    						  .build();
    }
	
	//Optimize this method if you can to remove redundant expiration parameter (include it in extraClaims)
    public String generateToken(Map<String, Object> extraClaims, long expiration, UserDetails userDetails) {
       return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractSubject(token);
        return (email.equals(userDetails.getUsername()) && isTokenExpired(token));
    }
    
    public Boolean isRefreshTokenValid(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank())
			return false;
		
    	return redisTemplate.hasKey(refreshToken);
    }

    public Boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }
    
	public Boolean isRefreshTokenExpired(String refreshToken) {
		return Long.parseLong((String) redisTemplate.opsForHash().get(refreshToken, "expiration")) < System.currentTimeMillis();
	}
    
	
	//if u can please store the token version in the refresh token
	public void saveRefreshToken(ResponseCookie refreshToken, String accessToken, UserDetails userDetails) {
		Map<String, Object> refreshData = new HashMap<>();
		refreshData.put("expiration", System.currentTimeMillis() + refreshToken.getMaxAge().toMillis());
		refreshData.put("access_token", accessToken);
		refreshData.put("email", userDetails.getUsername());
		redisTemplate.opsForHash().putAll(refreshToken.getValue(), refreshData);	
	}
	
	public void deleteRefreshToken(String refreshToken) {
		redisTemplate.delete(refreshToken);
	}

	
	public Map<Object, Object> getRefreshData(String refreshToken) {
		return redisTemplate.opsForHash().entries(refreshToken);
	}
	
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}