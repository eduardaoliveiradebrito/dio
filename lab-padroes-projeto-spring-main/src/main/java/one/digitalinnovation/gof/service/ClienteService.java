package one.digitalinnovation.gof.service;

import org.springframework.http.ResponseEntity;

import one.digitalinnovation.gof.model.Cliente;


public interface ClienteService {

	ResponseEntity<?>  buscarTodos();

	ResponseEntity<?> buscarPorId(Long id);

	void inserir(Cliente cliente);

	ResponseEntity<?> atualizar(Long id, Cliente cliente);

	void deletar(Long id);

}
