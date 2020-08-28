package br.com.fiap.persistence.services.pedido;

import java.util.List;

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.Pedido;

public interface IPedidoService {

	void deletePedido(long id);
	Pedido updatePedido(Pedido pedido);
	Pedido addPedido(Pedido pedido);
	List<Pedido> getAllPedido();
	Pedido getPedidoById(long id);
}
