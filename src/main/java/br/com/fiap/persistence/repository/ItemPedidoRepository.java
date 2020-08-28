package br.com.fiap.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.persistence.entity.ItemPedido;
/**
 * Interface do Spring Data responsável por manipular no banco via Spring Data a tabela ItemPedido
 * @author Willian Yoshiaki Kazahaya
 *
 */
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>  {

}
