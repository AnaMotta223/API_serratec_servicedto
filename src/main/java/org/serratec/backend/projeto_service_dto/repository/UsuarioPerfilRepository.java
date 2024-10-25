package org.serratec.backend.projeto_service_dto.repository;

import org.serratec.backend.projeto_service_dto.domain.UsuarioPerfil;
import org.serratec.backend.projeto_service_dto.domain.UsuarioPerfilPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, UsuarioPerfilPK>{

}
