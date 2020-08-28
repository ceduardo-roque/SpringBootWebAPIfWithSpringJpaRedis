package br.com.fiap.persistence.services.cliente;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;



import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.repository.ClienteRepository;
/**
 * Classe responsavel por manipular efetivamente o repositorio de Cliente
 * @author Sara Regina Pires
 *
 */

@Service
public class ClienteService implements IClienteService{
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	/**
	 * Método responsavel por adicionar um novo cliente
	 * @return cliente retorna um objeto da ientidade cliente
	 */
	@Override	
	@Caching(
		put= { @CachePut(value= "clienteCache", key= "#cliente.id") },
		evict= { @CacheEvict(value= "allClienteCache", allEntries= true) }
	)
	public Cliente addCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	/**
	 * Método responsavel por listar todos os clientes
	 * @return cliente retorna um objeto da ientidade cliente
	 */
	@GetMapping
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}

	/**
	 * Método responsavel localizar os clientes pelo id 
	 * @return cliente retorna um objeto da ientidade cliente
	 */
	@Override
	public Cliente findById(long id) {
		try {
			return clienteRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			System.out.println("Cliente não encontrado" + id);	
		}
		return null;
	}

	/**
	 * Método responsavel por deletar uma cliente da tabela cliente pelo seu id
	 * @return void com o cliente excluido
	 */
	public void deletarCliente(long id) {
		System.out.println("Excluir Cliente");
		this.clienteRepository.delete(this.clienteRepository.findById(id).get());
	}

	/**
	 * Método responsavel por atualizar os dados do cliente 
	 * @return cliente atualizado
	 */
	public Cliente updateCliente(Cliente cliente) {
		System.out.println("Atualizar Cliente");
		return this.clienteRepository.save(cliente);
	}

	/**
	 * Método responsavel por atualizar os dados do cliente 
	 * @return void cliente atualizado
	 */
	public void salvaCliente(Cliente cliente) {
		System.out.println("SalvaCliente");
		this.clienteRepository.save(cliente);
	}

}
