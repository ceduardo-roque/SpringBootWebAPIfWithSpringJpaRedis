package br.com.fiap.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.persistence.entity.Cliente;

/**
 * Interface do Spring Data responsável por manipular no banco via Spring Data a tabela Cliente
 * @author Sara Regina Pires
 *
 */

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
