package br.com.fiap.persistence.tests;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Categoria;
import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.ItemPedido;
import br.com.fiap.persistence.entity.Pedido;
import br.com.fiap.persistence.entity.Produto;
import br.com.fiap.persistence.enums.StatusPedido;

public class PedidoTest {
	private static String urlPedido = "http://localhost:8080/ecommerce_fiap/rest/pedido";
	public static void main(String[] args) {
		
		ClienteTest clienteTest = new ClienteTest();
		long idCliente = clienteTest.incluiCliente();
		
		Cliente cliente = new Cliente();
		cliente.setId(idCliente);
	
		Produto produto = new ProdutoTest().recuperaProduto(7);
		Produto produto2 = new ProdutoTest().recuperaProduto(19);
		
		Pedido pedido = new Pedido(cliente, LocalDateTime.now());
		pedido.addItemPedido(new ItemPedido(produto, 2));
		pedido.addItemPedido(new ItemPedido(produto2, 3));
		
		String pedidoId = criaPedido(pedido);
		System.out.println("Pedido Criado: " + pedidoId);
		Pedido pedidoPesquisado = getPedidoById(Integer.valueOf(pedidoId));
		System.out.println(pedidoPesquisado);
		
		Produto produto3 = new ProdutoTest().recuperaProduto(26);
		pedidoPesquisado.addItemPedido(new ItemPedido(produto3, 5));
		
		Pedido pedidoAtualizado = InsereItemPedido(pedidoPesquisado);
		System.out.println("Pedido atualizado com novo item");
		Pedido pedidoPesq2 = getPedidoById((int) pedidoAtualizado.getId());
		System.out.println(pedidoPesq2);
		
		System.out.println("Excluindo item do Pedido. Removendo o item 7 da lista");
		ItemPedido itemARemover = pedidoPesq2.getItensPedido().stream().filter(item -> item.getProduto().getId() == Long.parseLong("7")).findAny().orElse(null);
		RemoveItemPedido(pedidoPesq2.getId(), itemARemover);
		System.out.println("Item removido");
		Pedido pedidoPesq3 = getPedidoById((int) pedidoPesq2.getId());
		System.out.println(pedidoPesq3);
		
		System.out.println("Concluindo o pedido.");
		Pedido pedidoConcluido = concluiPedido(pedidoPesq3);
		System.out.println("Pedido concluido");
		System.out.println(pedidoConcluido);
	}
	
	private static String criaPedido(Pedido pedido) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		
		RestTemplate restTemplate = new RestTemplate();
		String url = PedidoTest.urlPedido;
		
		HttpEntity<Pedido> requestEntity = new HttpEntity<Pedido>(pedido, header);
		URI uri = restTemplate.postForLocation(url, requestEntity);
		
		String path = uri.getPath();
		System.out.println("Pedido criado");
		return path.substring(path.lastIndexOf("/") + 1);
		
	}
	
	private static Pedido getPedidoById(int id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPedido + "/{id}";
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Pedido> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Pedido.class, id);
		Pedido pedido = responseEntity.getBody();
		
		return pedido;
	}
	
	private static Pedido concluiPedido(Pedido pedidoConcluir) {
		pedidoConcluir.setStatus(StatusPedido.CONCLUIDO);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPedido + "/{id}/concluir";
		
		HttpEntity<Pedido> requestEntity = new HttpEntity<Pedido>(pedidoConcluir, headers);
		ResponseEntity<Pedido> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Pedido.class, pedidoConcluir.getId());
		Pedido pedido = responseEntity.getBody();
		
		return pedido;
	}
	
	private static Pedido InsereItemPedido(Pedido pedidoAtualizar) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPedido + "/{id}";
		
		HttpEntity<Set<ItemPedido>> requestEntity = new HttpEntity<Set<ItemPedido>>(pedidoAtualizar.getItensPedido(), headers);
		ResponseEntity<Pedido> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Pedido.class, pedidoAtualizar.getId());
		Pedido pedido = responseEntity.getBody();
		
		return pedido;
	}
	
	private static void RemoveItemPedido(Long idPedido ,ItemPedido itemAExcluir) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPedido + "/{idPedido}/item/{idItem}";
		
		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, idPedido, itemAExcluir.getId());
		
	}

}
