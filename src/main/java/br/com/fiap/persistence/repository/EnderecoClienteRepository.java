package br.com.fiap.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.EnderecoCliente;
/**
 * Interface do Spring Data responsável por manipular no banco via Spring Data a tabela EnderecoCliente
 * @author Sara Regina Pires
 *
 */
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long>{

	void deleteById(Cliente cliente);
	
	

}
