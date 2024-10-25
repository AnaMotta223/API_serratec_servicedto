package org.serratec.backend.projeto_service_dto.repository;

import org.serratec.backend.projeto_service_dto.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	
	Endereco findByCep(String cep);
}
