package com.minewaku.trilog.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenicationFilter jwtAuthFilter;

    @Autowired
    private RequestThrottleFilter requestThrottleFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        return http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/swagger-ui/**")
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**")
                        .permitAll()

                        // .requestMatchers("GET", "/api/v1/admin/user/**").hasAnyAuthority("USER_READ")
                        // .requestMatchers("PUT", "/api/v1/admin/user/**").hasAnyAuthority("USER_UPDATE")
                        // .requestMatchers("DELETE", "/api/v1/admin/user/**").hasAnyAuthority("USER_DELETE")
                        // .requestMatchers("POST", "/api/v1/admin/user/**").hasAnyAuthority("USER_CREATE")

                        // .requestMatchers("GET", "/api/v1/admin/role/**").hasAnyAuthority("ROLE_READ")
                        // .requestMatchers("PUT", "/api/v1/admin/role/**").hasAnyAuthority("ROLE_UPDATE")
                        // .requestMatchers("DELETE", "/api/v1/admin/role/**").hasAnyAuthority("ROLE_DELETE")
                        // .requestMatchers("POST", "/api/v1/admin/role/**").hasAnyAuthority("ROLE_CREATE")

                        // .requestMatchers("GET", "/api/v1/admin/permission/**").hasAnyAuthority("PERMISSION_READ")
                        // .requestMatchers("PUT", "/api/v1/admin/permission/**").hasAnyAuthority("PERMISSION_UPDATE")
                        // .requestMatchers("DELETE", "/api/v1/admin/permission/**").hasAnyAuthority("PERMISSION_DELETE")
                        // .requestMatchers("POST", "/api/v1/admin/permission/**").hasAnyAuthority("PERMISSION_CREATE")

                        .anyRequest()
                        .authenticated())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(requestThrottleFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}