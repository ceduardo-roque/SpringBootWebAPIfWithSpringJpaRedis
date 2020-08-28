package br.com.fiap.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.persistence.entity.Secao;

/**
 * Interface do Spring Data responsável por manipular no banco via Spring Data a tabela Secao
 * @author Carlos Eduardo Roque da Silva
 *
 */
public interface SecaoRepository extends JpaRepository<Secao, Long>{ 
	
}
