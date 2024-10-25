package org.serratec.backend.projeto_service_dto.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.serratec.backend.projeto_service_dto.domain.Usuario;
import org.serratec.backend.projeto_service_dto.dto.UsuarioDTO;
import org.serratec.backend.projeto_service_dto.dto.UsuarioInserirDTO;
import org.serratec.backend.projeto_service_dto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listar(){
		//anotacao so pode ser no controller, codigo abaixo é geral
		//pega o contexto de segurança e o login da pessoa (email, nome, etc)
		//@AuthenticationPrincipal UserDetails details
		UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("Login do usuario> " + details.getUsername());
		return ResponseEntity.ok(usuarioService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> buscar(@PathVariable Long id){
		Optional<Usuario> usuarioOpt = usuarioService.buscar(id);
		
		if(usuarioOpt.isPresent()) {
			UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioOpt.get());
			return ResponseEntity.ok(usuarioDTO);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> inserir(@Valid @RequestBody UsuarioInserirDTO usuarioInserirDTO){
		UsuarioDTO usuarioDTO = usuarioService.inserir(usuarioInserirDTO);
		
		//cria uma uri com o id e substitui o id pelo id do usuario ("usuarios/id")
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
				.buildAndExpand(usuarioDTO.getId()).toUri();
		
		//retorna o status como created
		return ResponseEntity.created(uri).body(usuarioDTO);
	}
}
