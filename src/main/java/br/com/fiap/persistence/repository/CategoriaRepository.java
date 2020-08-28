package br.com.fiap.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.persistence.entity.Categoria;

/**
 * Interface do Spring Data responsável por manipular no banco via Spring Data a tabela Categoria
 * @author Carlos Eduardo Roque da Silva
 *
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{ 
	
	Categoria findByDescricao(String descricao);	

}
