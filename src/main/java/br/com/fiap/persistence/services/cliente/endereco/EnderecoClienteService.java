package br.com.fiap.persistence.services.cliente.endereco;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.EnderecoCliente;
import br.com.fiap.persistence.repository.ClienteRepository;
import br.com.fiap.persistence.repository.EnderecoClienteRepository;

/**
 * Classe responsavel por manipular efetivamente o repositorio de EnderecoCliente
 * 
 * @author Sara Regina Pires
 *
 */
@Service
public class EnderecoClienteService implements IEnderecoClienteService {

	
	@Autowired
	private EnderecoClienteRepository enderecoClienteRepository;
	private ClienteRepository cliRepository;
	
	public EnderecoCliente addEnderecoCliente(EnderecoCliente enderecoCliente) {
		return enderecoClienteRepository.save(enderecoCliente);
	}
	

	@Override
	public EnderecoCliente findById(long id) {
		try {
			return enderecoClienteRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			System.out.println("Dados do endereço não encontrado" + id);	
		}
		return null;
	}
	
	public void deletarEndereco(Cliente cliente) {
		System.out.println("Excluir endereço do cliente");
		this.enderecoClienteRepository.deleteById(this.cliRepository.findById(cliente.getId()).get());
	}
	
	public EnderecoCliente updateCliente(EnderecoCliente enderecoCliente) {
		System.out.println("Endereço do Cliente");
		return this.enderecoClienteRepository.save(enderecoCliente);
	}
	
	@Override
	public List<EnderecoCliente> findEnderecosByCliente(Cliente cliente) {
		//TODO metodo para buscar os endereços do cliente
		List<EnderecoCliente> lista = new ArrayList<>();
		enderecoClienteRepository.findAll().forEach(e -> {
			if (cliente == e.getCliente()) {
				lista.add(e);				
			}
		});
		return lista;

	}


	
	
	
	

	

}
