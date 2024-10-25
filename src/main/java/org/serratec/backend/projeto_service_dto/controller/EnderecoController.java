package org.serratec.backend.projeto_service_dto.controller;

import org.serratec.backend.projeto_service_dto.dto.EnderecoDTO;
import org.serratec.backend.projeto_service_dto.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
	
	@Autowired
	private EnderecoService enderecoService;
	
	@GetMapping("/{cep}")
	public ResponseEntity<EnderecoDTO> buscar(@PathVariable String cep) {
		EnderecoDTO enderecoDTO = enderecoService.buscar(cep);
		if (null == enderecoDTO) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(enderecoDTO);
		}
	}
	
}