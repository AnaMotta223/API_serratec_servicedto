package org.serratec.backend.projeto_service_dto.repository;

import java.util.List;

import org.serratec.backend.projeto_service_dto.domain.Funcionario;
import org.serratec.backend.projeto_service_dto.dto.FuncionarioSalarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	//seleciona tudo e pega onde o salario é >= o valor minimo e <= valor maximo
	//:nome é uma forma de referenciar uma variavel da funcao (valor)
	@Query("SELECT f FROM Funcionario f WHERE f.salario >= :valorMinimo AND f.salario <= :valorMaximo")
	Page<Funcionario> buscarSalario(Double valorMinimo, Double valorMaximo, Pageable pageable);
	
	//@Query(value = "SELECT * FROM Funcionario f WHERE f.salario >= :valorMinimo AND f.salario <= :valorMaximo", nativeQuery = true)
	//Page<Funcionario> buscarSalarioNativo(Double valorMinimo, Double valorMaximo, Pageable pageable);
	
	//concat concatena as strings do sql
	@Query("SELECT f FROM Funcionario f WHERE UPPER(f.nome) LIKE UPPER(CONCAT('%', :paramNome, '%'))")
	Page<Funcionario> buscarPorNome(String paramNome, Pageable pageable);

	Page<Funcionario> findByNomeContainingIgnoreCase(String paramNome, Pageable pageable);
	
	@Query(value = """
			SELECT 
				date_part('year', age(now(), data_nascimento)) as idade,
				avg(salario) as mediaSalario,
				min(salario) as menorSalario,
				max(salario) as maiorSalario,
				count(*) as totalFuncionarios
			FROM funcionario
			GROUP BY idade
			HAVING count(*) > 1
			ORDER BY idade desc
			"""
			, nativeQuery = true)
	List<FuncionarioSalarioDTO> buscarSalariosPorIdade();
	
}
