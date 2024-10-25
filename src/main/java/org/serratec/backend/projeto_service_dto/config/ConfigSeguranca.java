package org.serratec.backend.projeto_service_dto.config;

import java.util.Arrays;

import org.serratec.backend.projeto_service_dto.security.JwtAuthenticationFilter;
import org.serratec.backend.projeto_service_dto.security.JwtAuthorizationFilter;
import org.serratec.backend.projeto_service_dto.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//diz que é uma classe de configuração e indica que vai ter segurança para serviços web
@Configuration
@EnableWebSecurity
public class ConfigSeguranca {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//desabilitamos o csrf e habilitamos o cors na camada de filtro
		http.csrf(csrf -> csrf.disable())
		.cors((cors) -> cors.configurationSource(corsConfigurationSource()))
		.authorizeHttpRequests(authorize -> 
	        authorize
	            .requestMatchers(HttpMethod.GET, "/funcionarios").permitAll()
	            .requestMatchers(HttpMethod.POST, "/enderecos/**").permitAll()
	            .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
	            .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.GET, "/funcionarios/nome").hasAnyAuthority("ADMIN", "USER")
	            .anyRequest().authenticated()
	    )
	    .httpBasic(Customizer.withDefaults())
	    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		JwtAuthenticationFilter jwtAuthenticationFilter = 
				new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))
						, jwtUtil);
		jwtAuthenticationFilter.setFilterProcessesUrl("/login");
		
		JwtAuthorizationFilter jwtAuthorizationFilter = 
				new JwtAuthorizationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
						jwtUtil, userDetailsService);
		
		http.addFilter(jwtAuthenticationFilter);
		http.addFilter(jwtAuthorizationFilter);
		
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		//dominio(s) para poder acessar a api
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		//metodos de acesso
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//aplica para todos os endpoints
		source.registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues());
		
		return source;
	}
	
	//criptografia de senhas
	//faz com que a classe seja gerenciada pelo spring
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//permite a autenticacao personalizada que criamos no service
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
//	nao vai ser mais puxado da memoria, e sim do banco	
//	@Bean
//	InMemoryUserDetailsManager userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("teste")
//				.password("123456")
//				.roles("RH")
//				.build();
//		return new InMemoryUserDetailsManager(user);
//	}
//	
}
