package br.com.fiap.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.persistence.entity.Produto;
/**
 * Interface do Spring Data responsável por manipular no banco via Spring Data a tabela ItemPedido
 * @author Ayrton Henrique Gomes Silva 
 *
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}