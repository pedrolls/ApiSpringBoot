package com.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.model.Cliente;
import com.api.model.Emprestimo;
import com.api.repositorio.ClienteRepositorio;
import com.api.repositorio.EmprestimoRepositorio;

@Controller
@RequestMapping("/api/emprestimo")
public class EmprestimoController {

	@Autowired
	EmprestimoRepositorio emprestimoRepositorio;

	@Autowired
	ClienteRepositorio clienteRepositorio;

	@PostMapping
	public ResponseEntity<Emprestimo> salvarEmprestimo(@RequestBody Emprestimo emprestimo) {
		
		Cliente novoCLiente = clienteRepositorio.findByCpf(emprestimo.getCliente().getCpf());
		
		if(novoCLiente != null)
			emprestimo.setCliente(novoCLiente);
		else
			novoCLiente = clienteRepositorio.save(emprestimo.getCliente());
		
		emprestimo.setCliente(novoCLiente);
		
		Emprestimo novoEmprestimo = emprestimoRepositorio.save(emprestimo);

		URI location = getUri(novoEmprestimo.getId());
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{cpf}")
	public ResponseEntity<Emprestimo> listarEmprestimoPorCliente(@PathVariable("cpf") String cpf) {
		try {
			Emprestimo emprestimo = emprestimoRepositorio.findEmprestimoByCpf(cpf);
			return emprestimo != null ? ResponseEntity.ok(emprestimo) : ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Emprestimo>> listarTodosEmprestimos(){
		return ResponseEntity.ok(emprestimoRepositorio.findAll());
	}

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}

}
