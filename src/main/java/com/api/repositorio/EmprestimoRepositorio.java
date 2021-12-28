package com.api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.model.Emprestimo;

public interface EmprestimoRepositorio extends JpaRepository<Emprestimo, Long>{
	
	@Query(
			value = "select * from Emprestimo e where e.cliente_emprestimo = ?1", nativeQuery = true
			)
	public Emprestimo findEmprestimoByCpf(String cpf);

}
