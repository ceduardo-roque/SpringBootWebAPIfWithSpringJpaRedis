package br.com.fiap.persistence.services.cliente;

import java.util.List;

import br.com.fiap.persistence.entity.Cliente;

/**
 * Interface responsável por chamar efetivamente as ações de CRUD de cliente com métodos do Spring Data via Reflection
 * @author Sara Regina Pires
 *
 */
public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Cliente addCliente(Cliente cliente);

	public Cliente findById(long id);
	
	public void deletarCliente(long id);
	
	public void salvaCliente(Cliente cliente);
	
	public Cliente updateCliente(Cliente cliente);
	
}
