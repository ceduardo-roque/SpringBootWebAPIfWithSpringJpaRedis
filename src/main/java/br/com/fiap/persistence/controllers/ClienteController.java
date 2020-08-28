package br.com.fiap.persistence.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.EnderecoCliente;
import br.com.fiap.persistence.services.cliente.IClienteService;
import br.com.fiap.persistence.services.cliente.endereco.IEnderecoClienteService;

/**
 * Classe que uma controller que pode ser invocada via chamadas REST com JSON
 * @author Carlos Eduardo Roque da Silva / Sara Regina Pires
 *
 */
@RestController
@RequestMapping("rest")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IEnderecoClienteService enderecoClienteService;

	/**
	 * Método que cadastra um novo cliente 
	 * @param cliente Cliente para ser cadastrado 
	 * @param builder Builder injetado automaticamente pelo Spring pra manipulação de caminho relativo
	 * @return Um objeto ResponseEntity com status de CREATED
	 */
	@PostMapping("cliente")
	public ResponseEntity<Void> addCliente(@RequestBody Cliente cliente, UriComponentsBuilder builder) {

		HttpHeaders headers = new HttpHeaders();
		try {
			if (cliente.getEnderecos().size() == 0) {
				System.out.println("Cliente deve possuir um endereço.");
				return new ResponseEntity<Void>(headers, HttpStatus.BAD_REQUEST);
			} else {
				List<EnderecoCliente> novaLista = new ArrayList<EnderecoCliente>();
				// Limpa os endereços pra depois adicionar
				for (EnderecoCliente enderecoCliente : cliente.getEnderecos()) {
					novaLista.add(enderecoCliente);
				}
				cliente.getEnderecos().clear();
				
				// Adiciona o cliente e pega o cliente Managed
				Cliente novoCliente = clienteService.addCliente(cliente);
				for (EnderecoCliente enderecoCliente : novaLista) {
					enderecoCliente.setCliente(novoCliente);
					novoCliente.addEndereco(enderecoCliente);
				}

				//Salva o cliente com a sua lista de endereços dentro
				clienteService.salvaCliente(novoCliente);
				
				headers.setLocation(builder.path("/cliente/{id}").buildAndExpand(novoCliente.getId()).toUri());
				return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Método que retorna um cliente pelo Id
	 * @param id do cliene a ser pesquisada
	 * @return o cliente encontrado ou Status de sem conteúdo caso não encontre
	 */
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable("id") Long id) {
		System.out.println("Código Cliente: " + id);
		Cliente cliente = this.clienteService.findById(id);
		if (cliente != null) {
			return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
		} else {
			return new ResponseEntity<Cliente>(cliente, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Método que retorna todos cliente
	 * @return Um objeto ResponseEntity com status OK
	 */
	@GetMapping("cliente")
	public ResponseEntity<List<Cliente>> getAllClientes() {
		System.out.println("Listar Todos os clientes");
		List<Cliente> listagemCliente = this.clienteService.findAll();
		return new ResponseEntity<List<Cliente>>(listagemCliente, HttpStatus.OK);

	}
	

	@PutMapping("cliente")
	public ResponseEntity<Cliente> updateCliente(@RequestBody Cliente cliente) {
		System.out.println("UPDATE: " + cliente);
		Cliente clienteParaAtualizar = this.clienteService.findById(cliente.getId());
		if(clienteParaAtualizar !=null) {
			this.clienteService.updateCliente(cliente);
			return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
		} else return new ResponseEntity<Cliente>(cliente, HttpStatus.NOT_FOUND);
	}
	

	@DeleteMapping("cliente/{id}")
	public ResponseEntity<Void> deleteCliente(@PathVariable("id") Long id) {
		System.out.println("DELETE CLIENTE: " + id);
		Cliente clienteParaAtualizar = this.clienteService.findById(id);
		if(clienteParaAtualizar!=null) {
			this.clienteService.deletarCliente(id);
			return new ResponseEntity<Void>(HttpStatus.OK);			
		} else 
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

}
