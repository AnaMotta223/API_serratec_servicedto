package org.serratec.backend.projeto_service_dto.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(EmailException.class)
	private ResponseEntity<Object> handleEmailException(EmailException ex){
		//unprocessableEntity significa que poderia armazenar mas algo não permite (estado 422) - no caso email nao unico
		return ResponseEntity.unprocessableEntity().body(ex.getMessage());
	}
	
	@ExceptionHandler(SenhaException.class)
	private ResponseEntity<Object> handleSenhaException(SenhaException ex){
		return ResponseEntity.unprocessableEntity().body(ex.getMessage());
	}
}
