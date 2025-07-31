package com.minewaku.trilog.config.filters;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minewaku.trilog.dto.common.response.ErrorResponse;
import com.minewaku.trilog.exception.ApiException;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenicationFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ErrorUtil errorUtil;
    
    private final List<String> PUBLIC_URLS = Arrays.asList(
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
        );
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    private boolean isPublicUrl(String requestUri) {
        return PUBLIC_URLS.stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }
    
    private void sendErrorResponse(HttpServletResponse response, ApiException errorDetail) throws IOException {
    	ErrorResponse errorResponse = ErrorResponse.builder().errorCode(errorDetail.getErrorCode()).message(errorDetail.getMessage()).timestamp(ZonedDateTime.now(ZoneId.of("Z"))).build();
        
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt; //it means token
        final String email;

        // login doesn't need to have token
        String requestUri = request.getRequestURI();
        if (isPublicUrl(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
        	sendErrorResponse(response, errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_JWT_TOKEN));
            return;
        }
        
        try {
        	jwt = authHeader.substring(7);
            logger.info("incoming JWT: " + jwt); 
            email = jwtUtil.extractSubject(jwt);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    //creating an authentication object
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //principle, credentials and authorization
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
		} catch (Exception e) {
			logger.error("Error: ", e);
			sendErrorResponse(response, errorUtil.ERROR_DETAILS.get(errorUtil.INVALID_JWT_TOKEN));
		}
    }
}
