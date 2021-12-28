package com.api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.model.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{

	public Cliente findByCpf(String cpf);
}
