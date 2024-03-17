package com.project.shopapp.configurations;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	@Value("${api.prefix}")
	private String apiPrefix;
	
	private final JwtTokenFilter jwtTokenFilter;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
	            .authorizeHttpRequests(requests -> {
	                requests.requestMatchers(String.format("%s/users/register", apiPrefix),
	                		String.format("%s/users/login", apiPrefix))
	                .permitAll()


                    .requestMatchers("GET",
                            String.format("%s/roles**", apiPrefix)).permitAll()

	                //categories
	                .requestMatchers("GET",String.format("%s/categories/**", apiPrefix)).permitAll()
	                .requestMatchers("POST",String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
	                .requestMatchers("PUT",String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
	                .requestMatchers("DELETE",String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
	                
	                
	                //products
	                .requestMatchers("GET",String.format("%s/products/**", apiPrefix)).permitAll()
	                .requestMatchers("GET",String.format("%s/products/images/**", apiPrefix)).permitAll()
	                .requestMatchers("POST",String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
	                .requestMatchers("PUT",String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
	                .requestMatchers("DELETE",String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
	                
	                //orders
	                .requestMatchers("GET",String.format("%s/orders/**", apiPrefix)).permitAll()
	                .requestMatchers("POST",String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
	                .requestMatchers("PUT",String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
	                .requestMatchers("DELETE",String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
	                
	                //order-details
	                .requestMatchers("GET",String.format("%s/order_details/**", apiPrefix)).permitAll()
	                .requestMatchers("POST",String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER)
	                .requestMatchers("PUT",String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
	                .requestMatchers("DELETE",String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
	                
	                .anyRequest().authenticated();
	                
	            }).csrf(AbstractHttpConfigurer::disable);
                http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
					
					@Override
					public void customize(CorsConfigurer<HttpSecurity> httpSecurityCrosConfigurer) {
						CorsConfiguration configuration = new CorsConfiguration();
						configuration.setAllowedOrigins(List.of("*"));
						configuration.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","PATCH","OPTIONS"));
						configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
						configuration.setExposedHeaders(List.of("x-auth-token"));
						UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
						source.registerCorsConfiguration("/**", configuration);
						httpSecurityCrosConfigurer.configurationSource(source);
						
					}
				});
	            return http.build();
	}
}
