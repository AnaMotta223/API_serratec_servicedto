package org.serratec.backend.projeto_service_dto.controller;

import java.io.IOException;
import java.util.List;

import org.serratec.backend.projeto_service_dto.domain.Foto;
import org.serratec.backend.projeto_service_dto.domain.Funcionario;
import org.serratec.backend.projeto_service_dto.dto.FuncionarioDTO;
import org.serratec.backend.projeto_service_dto.dto.FuncionarioSalarioDTO;
import org.serratec.backend.projeto_service_dto.repository.FuncionarioRepository;
import org.serratec.backend.projeto_service_dto.service.FotoService;
import org.serratec.backend.projeto_service_dto.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
	

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private FotoService fotoService;
	
	@GetMapping
	public ResponseEntity<List<FuncionarioDTO>> listar() {
		return ResponseEntity.ok(funcionarioService.listar());
	}
	
	@GetMapping("/{id}/foto")
	public ResponseEntity<byte[]> buscarFoto(@PathVariable Long id){
		Foto foto = fotoService.buscarPorIdFuncionario(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, foto.getTipo());
		headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(foto.getDados().length));
		return new ResponseEntity<>(foto.getDados(), headers, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FuncionarioDTO> buscar(@PathVariable Long id) {
		FuncionarioDTO funcionario = funcionarioService.buscar(id);
		if (null == funcionario) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(funcionario);
	}
	
	//request part pega o multipartfile
	//consumes indica que como vc recebe um multipart ele muda o tipo - passa a nao ser json
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<FuncionarioDTO> inserir(@RequestPart MultipartFile file, @RequestPart Funcionario funcionario) throws IOException{
		return ResponseEntity.ok(funcionarioService.inserir(funcionario, file));
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	//Codigo antigo
	
	/*
	@GetMapping
	public ResponseEntity<List<Funcionario>> listar() {
		List<Funcionario> funcionarios = funcionarioRepository.findAll();
		return ResponseEntity.ok(funcionarios);
	}
	*/
	
	
	@GetMapping("/salario")
	public ResponseEntity<Page<Funcionario>> listarSalarios(@RequestParam(defaultValue = "0") Double valorMinimo,
			@RequestParam(defaultValue = "1000") Double valorMaximo, Pageable pageable) {
		Page<Funcionario> funcionarios = funcionarioRepository.buscarSalario(valorMinimo, valorMaximo, pageable);
		return ResponseEntity.ok(funcionarios);
	}
	
	@GetMapping("/nome")
	public ResponseEntity<Page<Funcionario>> buscarPorNome(@RequestParam(defaultValue = "a") String paramNome, Pageable pageable){
		Page<Funcionario> funcionarios = funcionarioRepository.buscarPorNome(paramNome, pageable);
		return ResponseEntity.ok(funcionarios);
	}
	
	@GetMapping("/salarios-por-idade")
	public ResponseEntity<List<FuncionarioSalarioDTO>> buscarSalarioPorIdade(){
		return ResponseEntity.ok(funcionarioRepository.buscarSalariosPorIdade());
	}
}
