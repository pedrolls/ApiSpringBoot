package com.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.model.Cliente;
import com.api.repositorio.ClienteRepositorio;

@Controller
@RequestMapping("/api/cliente")
public class ClienteController {

	@Autowired
	ClienteRepositorio clienteRepositorio;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodosOsClientes(){
		return clienteRepositorio.findAll().isEmpty() ?  ResponseEntity.noContent().build() : ResponseEntity.ok(clienteRepositorio.findAll());
	}
	
	@PostMapping
	public ResponseEntity<Cliente> salvarCliente(@RequestBody Cliente cliente){
		Cliente clienteRecuperado = clienteRepositorio.findByCpf(cliente.getCpf());
		
		if(clienteRecuperado == null) {
			clienteRecuperado = clienteRepositorio.save(cliente);
		}else {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		URI location = getUri(clienteRecuperado.getCpf());
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity<Cliente> listarClientePorCpf(@PathVariable("cpf") String cpf){
		Cliente clienteRecuperado;
		try {
				clienteRecuperado = clienteRepositorio.findByCpf(cpf);
				return	clienteRecuperado != null ? ResponseEntity.ok(clienteRecuperado) : ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}	
	}
	
	private URI getUri(String cpf) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{cpf}").buildAndExpand(cpf).toUri(); 
	}
}
