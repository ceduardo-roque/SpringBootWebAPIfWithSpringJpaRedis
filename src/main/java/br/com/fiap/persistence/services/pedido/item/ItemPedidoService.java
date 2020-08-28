package br.com.fiap.persistence.services.pedido.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import br.com.fiap.persistence.entity.ItemPedido;
import br.com.fiap.persistence.entity.Pedido;
import br.com.fiap.persistence.entity.Produto;
import br.com.fiap.persistence.repository.ItemPedidoRepository;
import br.com.fiap.persistence.repository.PedidoRepository;

@Service
public class ItemPedidoService implements IItemPedidoService {

	@Autowired
    private ItemPedidoRepository itemPedidoRepository;

	@Override
	@Caching(evict = { @CacheEvict(value = "itemPedidoCache", key = "#item.getId()"),
			@CacheEvict(value = "allItemPedidoCache", allEntries = true) })	
	public void deleteItemPedido(ItemPedido item) {
		itemPedidoRepository.deleteById(item.getId());
	}

	@Override
	public ItemPedido saveItemPedido(ItemPedido item) {
		return itemPedidoRepository.save(item);
	}

	@Override
	public List<ItemPedido> getAllItemPedido() {
		return itemPedidoRepository.findAll();
	}

	@Override
	@Cacheable(value="itemPedidoCache", key="#id")
	public ItemPedido getItemPedidoById(long id) {
		Optional<ItemPedido> item = itemPedidoRepository.findById(id);
		return item.isPresent() ? item.get() : null;
	}

	@Cacheable(value="itemPedidoCache", key = "#pedido.id")
	public List<ItemPedido> getItemPedidoByPedido(Pedido pedido) {
		List<ItemPedido> lista = new ArrayList<>();
		itemPedidoRepository.findAll().forEach(e -> {
			if (pedido == e.getPedido()) {
				lista.add(e);				
			}
		});
		return lista;
	}

	@Cacheable(value="itemPedidoCache", key = "#pedido.id")	
	public ItemPedido getItemPedidoFromPedidoByProduto(Pedido pedido, Produto produto) {
		List<ItemPedido> lista = new ArrayList<>();
		 
		itemPedidoRepository.findAll().forEach(e -> {
			if (pedido == e.getPedido()) {
				lista.add(e);				
			}
		});
		
		for (ItemPedido itemPedido : lista) {
			if (itemPedido.getProduto().getId() == produto.getId()) {
				return itemPedido;
			}
		}

		return null;
	}

}
