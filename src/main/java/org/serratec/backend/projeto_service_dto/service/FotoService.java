package org.serratec.backend.projeto_service_dto.service;

import java.io.IOException;
import java.util.Optional;

import org.serratec.backend.projeto_service_dto.domain.Foto;
import org.serratec.backend.projeto_service_dto.domain.Funcionario;
import org.serratec.backend.projeto_service_dto.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

@Service
public class FotoService {
	
	@Autowired
	private FotoRepository fotoRepository;
	
	//multipartfile é o jeito de poder enviar a foto 
	//io significa input output exception
	public Foto inserir(Funcionario funcionario, MultipartFile file) throws IOException{
		Foto foto = new Foto();
		foto.setNome(file.getName());
		foto.setTipo(file.getContentType());
		foto.setDados(file.getBytes());
		foto.setFuncionario(funcionario);
		return fotoRepository.save(foto);
	}
	
	//transactional é bom com grandes volumes de dados
	//se acontecer um erro no metodo ele dá um rollback e desfaz a operacao
	@Transactional
	public Foto buscarPorIdFuncionario(Long id) {
		Funcionario funcionario = new Funcionario();
		funcionario.setId(id);
		Optional<Foto> foto = fotoRepository.findByFuncionario(funcionario);
		if(foto.isEmpty()) {
			return null;
		}
		return foto.get();
	}
}
