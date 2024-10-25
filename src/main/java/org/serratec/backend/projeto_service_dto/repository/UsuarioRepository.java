package org.serratec.backend.projeto_service_dto.repository;

import org.serratec.backend.projeto_service_dto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	//select * from usuario where u.email = email
	Usuario findByEmail(String email);
}
