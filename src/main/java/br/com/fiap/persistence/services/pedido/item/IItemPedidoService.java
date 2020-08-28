package br.com.fiap.persistence.services.pedido.item;

import java.util.List;

import br.com.fiap.persistence.entity.ItemPedido;
import br.com.fiap.persistence.entity.Pedido;
import br.com.fiap.persistence.entity.Produto;

public interface IItemPedidoService {
	void deleteItemPedido(ItemPedido item);
	ItemPedido saveItemPedido(ItemPedido item);
	List<ItemPedido> getAllItemPedido();
	ItemPedido getItemPedidoById(long id);
}
