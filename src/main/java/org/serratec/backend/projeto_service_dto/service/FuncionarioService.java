package org.serratec.backend.projeto_service_dto.service;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.serratec.backend.projeto_service_dto.domain.Funcionario;
import org.serratec.backend.projeto_service_dto.dto.FuncionarioDTO;
import org.serratec.backend.projeto_service_dto.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FuncionarioService {
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FotoService fotoService;
	
	public List<FuncionarioDTO> listar(){
		List<FuncionarioDTO> funcionarios = funcionarioRepository.findAll().stream()
		.map(f -> adicionarImagemUri(f)).toList();
		
		return funcionarios;
	}
	
	public FuncionarioDTO buscar(Long id) {
		Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(id);
		if(funcionarioOpt.isEmpty()) {
			return null;
		}
		return adicionarImagemUri(funcionarioOpt.get());
	}
	
	public FuncionarioDTO inserir(Funcionario funcionario, MultipartFile file) throws IOException {
		funcionario = funcionarioRepository.save(funcionario);
		fotoService.inserir(funcionario, file);
		return adicionarImagemUri(funcionario);
	}
	
	//converte a imagem de funcionario no DTO
	//busca a foto pelo funcionario 
	public FuncionarioDTO adicionarImagemUri(Funcionario funcionario) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/funcionarios/{id}/foto")
				.buildAndExpand(funcionario.getId())
				.toUri();
		
		FuncionarioDTO dto = new FuncionarioDTO();
		dto.setNome(funcionario.getNome());
		dto.setDataNascimento(funcionario.getDataNascimento());
		dto.setSalario(funcionario.getSalario());
		//converte o uri de objeto java para string a ser mostrada
		dto.setUrl(uri.toString());
		return dto;
	}
}
