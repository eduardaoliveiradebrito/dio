package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;


@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ViaCepService viaCepService;

	@Override
	public ResponseEntity<?> buscarTodos() {
	    var clientes = clienteRepository.findAll();
	    
	    if (clientes.isEmpty()) {
	        return ResponseEntity.badRequest().body("Não existem clientes cadastrados");
	    }
	    
	    return ResponseEntity.ok(clientes);
	}


	@Override
	public ResponseEntity<?> buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isEmpty()) {
	        return ResponseEntity.badRequest().body("Cliente não encontrado");
		}
		return ResponseEntity.ok(cliente.get());
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public ResponseEntity<?>  atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isEmpty()) {
	        return ResponseEntity.badRequest().body("Cliente não encontrado");
		}

		salvarClienteComCep(cliente);
		return ResponseEntity.ok().build();
	}

	@Override
	public void  deletar(Long id) {
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}

}
