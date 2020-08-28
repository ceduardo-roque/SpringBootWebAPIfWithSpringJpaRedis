package br.com.fiap.persistence.services.cliente.endereco;

import java.util.List;

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.EnderecoCliente;

/**
 * Interface responsável por chamar efetivamente as ações de CRUD de EnderecoCliente com métodos do Spring Data via Reflection
 * @author Sara Regina Pires
 *
 */
public interface IEnderecoClienteService {

	EnderecoCliente findById(long id);
	
	public EnderecoCliente addEnderecoCliente(EnderecoCliente enderecoCliente);
	
	public void deletarEndereco(Cliente cliente);
	
	public EnderecoCliente updateCliente(EnderecoCliente enderecoCliente);
	
	List<EnderecoCliente> findEnderecosByCliente(Cliente cliente);
}
