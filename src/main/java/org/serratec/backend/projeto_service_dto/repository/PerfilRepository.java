package org.serratec.backend.projeto_service_dto.repository;

import org.serratec.backend.projeto_service_dto.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{

}
