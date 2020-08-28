package br.com.fiap.persistence.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.ItemPedido;
import br.com.fiap.persistence.entity.Pedido;
import br.com.fiap.persistence.entity.Produto;
import br.com.fiap.persistence.enums.StatusPedido;
import br.com.fiap.persistence.services.cliente.ClienteService;
import br.com.fiap.persistence.services.pedido.PedidoService;
import br.com.fiap.persistence.services.pedido.item.ItemPedidoService;
import br.com.fiap.persistence.services.produtos.ProdutoService;

/**
 * Classe que uma controller que pode ser invocada via chamadas REST com JSON
 * @author Willian Yoshiaki Kazahaya 
 *
 */
@RestController
@RequestMapping("rest")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private ItemPedidoService itemPedidoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	/**
	 * Retorna o Pedido passado pelo ID
	 * @param id - Código do Pedido
	 * @return ResponseEntity com o status OK para o pedido consultado pelo ID.
	 */
	@GetMapping("pedido/{id}")
	public ResponseEntity<Pedido> getPedidoById(@PathVariable("id") long id){
		Pedido pedido = pedidoService.getPedidoById(id);
		
        if (pedido != null)
            return new ResponseEntity<Pedido>(pedido, HttpStatus.OK);
        else
            return new ResponseEntity<Pedido>(pedido, HttpStatus.NO_CONTENT);
	}

	/**
	 * Retorna uma lista dos pedidos
	 * @return ResponseEntity com status OK com os pedidos
	 */
    @GetMapping("pedido")
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        return new ResponseEntity<List<Pedido>>(pedidoService.getAllPedido(), HttpStatus.OK);
    }

    /**
     * Criação do Pedido
     * @param pedido - Informações do Pedido
     * @param builder pegar as infações do objeto JSON 
     * @return ResponseEntity com o Status CREATED para pedido criado.
     */
    @PostMapping("pedido")
    public ResponseEntity<Void> addPedido(@RequestBody Pedido pedido, UriComponentsBuilder builder) {
    	Cliente cliente = pedido.getCliente();
    	Set<ItemPedido> itens = pedido.getItensPedido();
        HttpHeaders headers = new HttpHeaders();
        List<String> itensInseridos = new ArrayList<String>();
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setDataPedido(LocalDateTime.now());

        if (cliente == null) {
        	return new ResponseEntity<Void>(headers, HttpStatus.BAD_REQUEST);
        } else {
        	
        	Cliente clienteRef = clienteService.findById(cliente.getId());
        	pedido.setCliente(clienteRef);
        	
        	Pedido novoPedido = pedidoService.addPedido(pedido);
        	
        	Pedido pedidoInserido = pedidoService.getPedidoById(novoPedido.getId());
        	pedidoInserido.clearItens();
        	// Adiciona Itens de pedido
        	for (ItemPedido itemPedido : itens) {
        		String itemJaInserido = itensInseridos.stream().filter(item -> item.equalsIgnoreCase(String.valueOf(itemPedido.getProduto().getId()))).findAny().orElse(null);
        		
        		// Verifica se o item já foi inserido
        		if (itemJaInserido == null) {
        			
        			// Busca duplicatas do mesmo item para caso seja verdadeiro, somar as quantidades
	        		Set<ItemPedido> itensAtualizados = itens.stream().filter(item -> {
	        			if (item.getProduto().getId() == itemPedido.getProduto().getId()) {
	        				return true;
	        			}
	        			return false;
	        		}).collect(Collectors.toCollection(LinkedHashSet::new));
	
	        		int quantidade = 0;
	        		for (ItemPedido item : itensAtualizados) {
	        			quantidade += item.getQuantidade();        			
	        		}
	        		
	        		ItemPedido itemInserir = new ItemPedido();
	        		Produto produtoInserir = itemPedido.getProduto();
	        		
	        		itemInserir.setPedido(novoPedido);
	        		itemInserir.setProduto(produtoInserir);
	        		itemInserir.setQuantidade(quantidade);
	        		pedidoInserido.getItensPedido().add(itemInserir);
	        		itensInseridos.add(String.valueOf(itemPedido.getProduto().getId()));
        		}
			}

        	// Atualiza o pedido com os itens e os itens com o pedido
        	pedidoService.updatePedido(pedidoInserido);
        	
        	headers.setLocation(builder.path("/pedido/{id}").buildAndExpand(novoPedido.getId()).toUri());
        	return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        }
    }
    
    /**
     * Atualiza o pedido, verificando a quantidade em estoque quando o pedido for concluido
     * @param id ID pedido
     * @return ResponseEntity para status Ok de pedido atualizado e NOT FOUND para não encontrado.
     */
    @PutMapping("pedido/{id}/concluir")
    public ResponseEntity<Pedido> updatePedido(@PathVariable("id") long id ) {
        System.out.println("UPDATE: pedido " + id);
        Pedido pedidoAAtualizar= pedidoService.getPedidoById(id);
        if (pedidoAAtualizar != null) {
        	if (pedidoAAtualizar.getStatus() != StatusPedido.CONCLUIDO) {
        		long valorPedido = 0;
        		
        		Set<ItemPedido> itensPedido = pedidoAAtualizar.getItensPedido();
        		boolean contemEstoque = true;
        		for (ItemPedido item : itensPedido) {
        			Produto produto = produtoService.getProdutoById(item.getProduto().getId());
        			if (item.getQuantidade() > produto.getQuantidadeEstoque()) {
        				System.out.println("Não é possivel atualizar o produto por falta de estoque!");
        				contemEstoque = false;
        			} else {
        				valorPedido += produto.getPreco() * item.getQuantidade();
        			}
				}
        		
        		// Caso não tenha estoque, irá atualizar o status do pedido e retornar 400
        		if (!contemEstoque) {
        			pedidoAAtualizar.setStatus(StatusPedido.FALTAESTOQUE);
        			pedidoService.updatePedido(pedidoAAtualizar);
        			System.out.println("***********************************************************************************");
        			System.out.println("Não é possivel concluir o Pedido pois a quantidade em estoque é inferior ao pedido");
        			System.out.println("***********************************************************************************");
        			return new ResponseEntity<Pedido>(pedidoAAtualizar, HttpStatus.BAD_REQUEST);
        		} else { 
        			// Atualiza o estoque de cada produto
        			 itensPedido.forEach(item ->{
        				 Produto prodRemoverEstoque = produtoService.getProdutoById(item.getProduto().getId());
        				 prodRemoverEstoque.setQuantidadeEstoque(prodRemoverEstoque.getQuantidadeEstoque() - item.getQuantidade());
        				 
        				 produtoService.salvarProduto(prodRemoverEstoque);
        				 
        				 item.setPedido(pedidoAAtualizar);
        			 });
        			 // Conclui o pedido
        			 pedidoAAtualizar.setStatus(StatusPedido.CONCLUIDO);
        			 pedidoAAtualizar.setValorTotal(valorPedido);
        			 
        			// Atualiza os dados do pedido 
             		pedidoService.updatePedido(pedidoAAtualizar);	
        		}
        	} else {
        		pedidoService.updatePedido(pedidoAAtualizar);
        	}
            return new ResponseEntity<Pedido>(pedidoAAtualizar, HttpStatus.OK);
        } else
            return new ResponseEntity<Pedido>(pedidoAAtualizar, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Requisição para atualizar os itens do Pedido.
     * @param id - Código do Pedido
     * @param pedidoAtualiza itens - Array de Itens a serem atualizados/inseridos
     * @return  ResponseEntity com Status OK para o pedido ataulizado e NOT FOUND PARA NÃO ENCONTRADO.
     */
    @SuppressWarnings("unused")
	@PutMapping("pedido/{id}")
    public ResponseEntity<Pedido> updateItensPedido(@PathVariable("id") long id ,@RequestBody List<ItemPedido> itensNovos){
    	Pedido pedido = pedidoService.getPedidoById(id);
    	Set<ItemPedido> itensPedido = pedido.getItensPedido();
    	
    	if (pedido != null) {
    		// Verifica se o Pedido já foi concluido
    		if (pedido.getStatus() == StatusPedido.CONCLUIDO) {
    			return new ResponseEntity<Pedido>(pedido, HttpStatus.BAD_REQUEST);
    		} else {
	    		for (ItemPedido novoItem: itensNovos) {
	    			Produto buscaProduto = produtoService.getProdutoById(novoItem.getProduto().getId());
	    			
	    			// Verifica se o produto existe
	    			if (buscaProduto != null) {
		    			// Verifica se o Item passado existe no pedido
		    			ItemPedido itemEncontradoPedido = itensPedido.stream().filter(itemPedido -> {
														    					Produto prodPedido = itemPedido.getProduto();
														    					
														    					if (prodPedido.getId() == novoItem.getProduto().getId()) {
														    						return true;
														    					} else {
														    						return false;
														    					}
														    					
														    				}).findAny().orElse(null);
		    			
		    			// Se encontrou o item, substitui a quantidade pela informada no Body
		    			if (itemEncontradoPedido != null) {
		    				itemEncontradoPedido.setQuantidade(novoItem.getQuantidade());
		    				pedido.getItensPedido().add(itemEncontradoPedido);
		    			} else {
		    				// Caso contrario, seta o Pedido no item e salva
		    				novoItem.setPedido(pedido);
		    				novoItem.setProduto(buscaProduto);
		    				pedido.getItensPedido().add(novoItem);
		    			}
		    			
	    			}
	    		}
	    		// Atualiza os dados do pedido 
         		pedidoService.updatePedido(pedido);	
	    		return new ResponseEntity<Pedido>(pedido, HttpStatus.OK);
    		}
    	} else {
    		return new ResponseEntity<Pedido>(pedido, HttpStatus.NOT_FOUND);
    	}
    }
    
    /**
     * Exclusão dos Pedidos
     * @param id numero do pedido
     * @return ResponseEntity com status OK para pedido excluído e não encontrado para o pedido não encontrado.
     */
    @DeleteMapping("pedido/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable("id") Long id) {
        Pedido pedidoParaExcluir = pedidoService.getPedidoById(id);
                
        if (pedidoParaExcluir != null) {
            pedidoService.deletePedido(id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    /**
     * Remove um item de Pedido 
     * @param id idPedido - ID do pedido
     * @param codItem codProduto - ID do Produto
     * @return ResponseEntity com status OK para Item do pedido excluído e não encontrado para o pedido não encontrado.
     */
    @DeleteMapping("pedido/{idPedido}/item/{idItem}")
    public ResponseEntity<Void> deleteItemPedido(@PathVariable("idPedido") Long idPedido, @PathVariable("idItem") Long idItem) {
		Pedido pedido = pedidoService.getPedidoById(idPedido);
		Set<ItemPedido> itensPedido = pedido.getItensPedido();

		Set<ItemPedido> itensAtualizados = itensPedido.stream().filter(item -> {
			if (item.getId() == idItem) {
				return false;
			}
				
			return true;
		}).collect(Collectors.toCollection(LinkedHashSet::new));
		
        if (itensAtualizados != null) {
        	pedido.clearItens();
        	pedidoService.updatePedido(pedido);
        	
        	pedido.setItensPedido(itensAtualizados);
        	pedidoService.updatePedido(pedido);
        	return new ResponseEntity<Void>(HttpStatus.OK); 
        } else {
        	return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
        }
    }    
}
