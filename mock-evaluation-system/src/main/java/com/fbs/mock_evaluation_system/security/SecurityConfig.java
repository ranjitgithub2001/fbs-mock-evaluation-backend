package com.fbs.mock_evaluation_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final UserDetailsServiceImpl userDetailsService;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration config = new CorsConfiguration();
			config.addAllowedOrigin("http://localhost:3000");
			config.addAllowedMethod("*");
			config.addAllowedHeader("*");
			config.setAllowCredentials(true);
			config.addAllowedOrigin("https://your-app.vercel.app");
			return config;
		})).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll().requestMatchers("/auth/**")
						.permitAll().requestMatchers(HttpMethod.POST, "/trainer-requests").permitAll()
						.requestMatchers(HttpMethod.POST, "/ai/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.POST, "/reports/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers("/trainer-requests/**").hasRole("ADMIN").requestMatchers("/users/**")
						.hasRole("ADMIN").requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.GET, "/batches/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.GET, "/modules/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.GET, "/course-modules/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.GET, "/batch-modules/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.GET, "/evaluations/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.POST, "/evaluations").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.PUT, "/evaluations/**").hasAnyRole("ADMIN", "TRAINER")
						.requestMatchers(HttpMethod.POST, "/batches/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/modules/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/batch-modules/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/analytics/**").hasAnyRole("ADMIN", "TRAINER").anyRequest()
						.authenticated())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}