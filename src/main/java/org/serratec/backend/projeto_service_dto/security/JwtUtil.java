package org.serratec.backend.projeto_service_dto.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//diz que a classe vai ser gerenciada pelo spring
@Component
public class JwtUtil {
	
		@Value("${auth-jwt-secret}")
		private String jwtSecret;
		
		@Value("${auth-jwt-expiration-miliseg}")
		private Long jwtExpirationMiliseg;
		
		public String generateToken(String username) {
			SecretKey secreKeySpec = Keys.hmacShaKeyFor(jwtSecret.getBytes());
			return Jwts.builder()
					.setSubject(username)
					.setExpiration(new Date(System.currentTimeMillis() + 
							this.jwtExpirationMiliseg))
					.signWith(secreKeySpec)
					.compact();
		}
		
		public boolean isValidToken(String token) {
			//claims é a parte do meio do token
			Claims claims = getClaims(token);
			if (claims != null) {
				//verifica o nome, data de expiracao e data atual
				String username = claims.getSubject();
				Date expirationDate = claims.getExpiration();
				Date now = new Date(System.currentTimeMillis());
				//se nenhum for nulo e a data for valida o token é valido
				if (username != null && expirationDate != null && now.before(expirationDate)) {
					return true;
				}
			}
			return false;
		}
		
		public String getUserName(String token) {
			Claims claims = getClaims(token);
			if (claims != null) {
				return claims.getSubject();
			}
			return null;
		}
		
		public Claims getClaims(String token) {
			return Jwts.parserBuilder()
					.setSigningKey(jwtSecret.getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();
		}

	}
