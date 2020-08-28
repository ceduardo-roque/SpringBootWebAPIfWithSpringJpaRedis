package br.com.fiap.persistence.services.pedido;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import br.com.fiap.persistence.entity.Pedido;
import br.com.fiap.persistence.repository.PedidoRepository;

@Service
public class PedidoService implements IPedidoService{
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Override
	@Caching(
		evict= { @CacheEvict(value= "pedidoCache", key= "#id"),
				 @CacheEvict(value= "allPedidoCache", allEntries= true) }
	)
	public void deletePedido(long id) {
		Optional<Pedido> pedidoAExcluir = pedidoRepository.findById(id);
		Pedido pedido = pedidoAExcluir.get();
		this.pedidoRepository.delete(pedido);
	}

	@Override
	@Caching(
			put= { @CachePut(value= "pedidoCache", key= "#pedido.id") },
			evict= { @CacheEvict(value= "allPedidoCache", allEntries= true) }
	)
	public Pedido updatePedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	@Override
	@Caching(
			put = { @CachePut(value= "pedidoCache", key= "#pedido.id") },
			evict = { @CacheEvict(value= "allPedidoCache", allEntries= true) }
		)	
	public Pedido addPedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	@Override
	@Cacheable(value= "allPedidoCache", unless= "#result.size() == 0")
	public List<Pedido> getAllPedido() {
		return pedidoRepository.findAll();
	}

	@Override
    @Cacheable(value="pedidoCache", key = "#id")
	public Pedido getPedidoById(long id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		return pedido.isPresent() ? pedido.get() : null;
	}

}
