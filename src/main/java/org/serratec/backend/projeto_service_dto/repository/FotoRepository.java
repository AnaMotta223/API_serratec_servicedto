package org.serratec.backend.projeto_service_dto.repository;

import java.util.Optional;

import org.serratec.backend.projeto_service_dto.domain.Foto;
import org.serratec.backend.projeto_service_dto.domain.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoRepository extends JpaRepository<Foto, Long>{
	
	Optional<Foto> findByFuncionario(Funcionario funcionario);
}
