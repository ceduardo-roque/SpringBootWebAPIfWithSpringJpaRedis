package br.com.fiap.persistence.simulation.helpers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

public class PedidosHelper extends APIHelper {

	private String mensagem;

	public PedidosHelper() {
		super("pedido");
	}

	public void consultaTodosOsPedidos() {
		List<Pedido> pedidos = listaTodasOsPedidos();
		System.out.println("******************************************************************************");
		System.out.println("                           PEDIDOS CADASTRADOS");
		System.out.println("******************************************************************************");
		if (pedidos != null && !pedidos.isEmpty()) {
			for (Pedido pedido : pedidos) {
				System.out.println("      " + pedido);
			}
		} else {
			System.out.println("\nNão foram encontrados Pedidos cadastrados.\n");
		}
	}

	private List<Pedido> listaTodasOsPedidos() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Pedido[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Pedido[].class);

		Pedido[] pedidos = responseEntity.getBody();
		if (pedidos == null || pedidos.length == 0)
			System.out.println("\nNão foram encontrados Pedidos cadastrados.");
		return Arrays.asList(pedidos);
	}

	public String cadastraPedido(Pedido pedido) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Pedido> requestEntity = new HttpEntity<Pedido>(pedido, header);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		System.out.println("Pedido criado");
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public Pedido consultaPedidoPeloID(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Pedido> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Pedido.class,
				id);
		Pedido produto = responseEntity.getBody();

		return produto;

	}

	private void excluiPedido(Pedido pedido) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<Pedido> requestEntity = new HttpEntity<Pedido>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, pedido.getId());
	}

	public void atualizaPedido(Pedido pedido) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Pedido> requestEntity = new HttpEntity<Pedido>(pedido, headers);
		restTemplate.put(url, requestEntity);

	}

	public void montaMenuPedidos(String mensagem) {
		this.mensagem = mensagem;
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println("******************************************************************************");
			System.out.println("        		      	   PEDIDOS DA LIVRARIA");
			System.out.println("******************************************************************************");
			System.out.println("\n");
			System.out.println(" 1. Consultar Pedidos");
			System.out.println(" 2. Incluir Novo Pedido");
			System.out.println(" 3. Atualizar um Pedido");
			System.out.println(" 4. Excluir um Pedido");
			System.out.println(" 5. Concluir um pedido");
			System.out.println(" 6. Remover um item do pedido");

			System.out.println("\n");
			System.out.println(" 0. Voltar ao MENU Anterior");

			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			if (!valorLido.contentEquals("0"))
				this.trataLeituraTelaPedidos(valorLido);
			else
				processa = false;
		}
	}

	private void trataLeituraTelaPedidos(String valorLido) {
		switch (valorLido) {
		case "1":
			this.consultaTodosOsPedidos();
			break;
		case "2":
			this.cadastraNovoPedido();
			break;
		case "3":
			this.excluiPedido();
			break;
		case "5":
			this.concluirPedido();
			break;
		case "6":
			this.excluirItemPedido();
			break;
		default:
			mensagem = "Digite uma opção válida (1, 2, 3, 4 e 5)";
		}

	}

	private void excluiPedido() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;

		while (processa) {

			System.out.println(" Escolha o ID de um Pedido para EXCLUIR:");
			System.out.println("\n");
			this.consultaTodosOsPedidos();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if (valorLido == null || valorLido.length() == 0)
				System.out.println("ID do Pedido Inválido.");
			else {
				Pedido pedido = this.consultaPedidoPeloID(valorLido);
				if (pedido != null) {
					System.out.println("Deseja realmente excluir o Pedido: " + pedido + " (S/N)");
					valorLido = lerTela(s);
					if (valorLido == null || valorLido.length() == 0) {
						System.out.println("Opção inválida!");
						processa = false;
					} else {
						if (valorLido.equalsIgnoreCase("S")) {
							this.excluiPedido(pedido);
							processa = false;
							System.out.println("Pedido excluído com sucesso!!!");
						}
					}
				} else {
					System.out.println("Pedido com o ID: " + valorLido + " não encontrado.");
					processa = false;
				}
			}
		}

	}

	private void cadastraNovoPedido() {
		Scanner s = new Scanner(System.in);
		System.out.println("******************************************************************************");
		System.out.println("                           NOVO PEDIDO");
		System.out.println("******************************************************************************");
		ClienteHelper clienteHelper = new ClienteHelper();
		ProdutoHelper produtoHelper = new ProdutoHelper();

		boolean processa = true;
		while (processa) {
			boolean dadosOK = false;
			clienteHelper.consultaTodosOsClientes();
			System.out.println("Digite o ID do Cliente:");
			String id = lerTela(s);
			Cliente cliente = clienteHelper.consultaClientePeloID(id);
			if (cliente == null) {
				System.out.println("Cliente não encontrado.");
				processa = false;
			} else {
				System.out.println("Escolha os produtos a serem incluídos no Pedido:");
				boolean incluiProdutos = true;
				List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();
				while (incluiProdutos) {
					produtoHelper.consultaTodosOsProdutos();
					System.out.println("ID do Produto:");
					String idProduto = lerTela(s);
					Produto produto = produtoHelper.consultaProdutoPeloID(idProduto);
					if (produto == null)
						System.out.println("Produto de ID: " + idProduto + " não encontrado.");
					else {
						System.out.println("Quantidade:");
						String quantidade = lerTela(s);
						if(validaEstoque(quantidade, produto, itensPedido)) {
							ItemPedido itemPedido = new ItemPedido(produto, Integer.valueOf(quantidade));
							itensPedido.add(itemPedido);							
						} else {
							System.out.println("Estoque do produto indisponível para esta quantidade. \nProduto: " + produto.getDescricao() + " - Estoque: " + produto.getQuantidadeEstoque());
						System.out.println("Itens já solicitados neste Pedido do Produto solicitado: " + recuperaProdutoDaLista(itensPedido, produto));
						}
					}
					System.out.println("Deseja continuar incluindo Produtos? (S/N)");
					String respostaContinuaInclusaoProduto = lerTela(s);
					if (respostaContinuaInclusaoProduto.equalsIgnoreCase("N")) {
						incluiProdutos = false;
					}
				}
				if (itensPedido.isEmpty()) {
					System.out.println("Nenhum produto selecionado. Pedido Cancelado!");
					processa = false;
				} else {
					System.out.println("Pedido Fechado. Deseja finalizar o pedido? (S/N)");
					String respostaContinuaInclusaoProduto = lerTela(s);
					if (respostaContinuaInclusaoProduto.equalsIgnoreCase("N")) {
						incluiProdutos = false;
						System.out.println("Pedido Cancelado.");
					} else {
						// Monta o Pedido em si
						Pedido pedido = new Pedido();
						pedido.setCliente(cliente); // Add CLiente
						for (ItemPedido itemPedido : itensPedido) {
							pedido.addItemPedido(itemPedido);
						}
						String idCriado = cadastraPedido(pedido);
						processa = false;
						System.out.println("Pedido cadastrado com sucesso!!!");
						System.out.println("ID do Pedido: " + idCriado);
					}
					
				}
			}
		}
	}

	private int recuperaProdutoDaLista(List<ItemPedido> itensPedido, Produto produto) {
		for (ItemPedido itemPedido : itensPedido) {
			if(itemPedido.getProduto().getId()==produto.getId())
				return itemPedido.getQuantidade();
		}
		return 0;
	}

	private boolean validaEstoque(String quantidade, Produto produto, List<ItemPedido> itensPedido) {
		int totalComprado = 0;
		for (ItemPedido itemPedido : itensPedido) {
			if(itemPedido.getProduto().getId()==produto.getId()) {
				totalComprado+=itemPedido.getQuantidade();
			}
		}
		totalComprado += Long.valueOf(quantidade);
		return totalComprado <= produto.getQuantidadeEstoque();
	}

	private void atualizaPedido() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;

		while (processa) {
			System.out.println(" Pedidos cadastrados.");
			this.consultaTodosOsPedidos();
			System.out.println(" Escolha o ID de um Pedido para atualizar:");
			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if (valorLido == null || valorLido.length() == 0)
				System.out.println("ID do Pedido Inválido.");
			else {
				Pedido pedido = this.consultaPedidoPeloID(valorLido);
				if (pedido != null) {
					System.out.println("Digite o novo nome para o Produto:");
					valorLido = lerTela(s);
					if (valorLido == null || valorLido.length() == 0) {
						// System.out.println("Novo nome inválido!");
						processa = false;
					} else {

						processa = false;
						this.atualizaPedido(pedido);
					}
				} else {
					System.out.println("Pedido de ID " + valorLido + " não encontrado!");
					processa = false;
				}
			}
		}
	}
	//this.concluirPedido();

	private void concluirPedido() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		
		while (processa) {
			this.consultaTodosOsPedidos();
			System.out.println("Selecione o pedido que deseja concluir");
			System.out.println("Se deseja voltar digite 0.");
			String id = lerTela(s);
			
			if (id == null) {
				System.out.println("Informe um pedido.");
			} else if (id.equals("0")){
				processa = false;
			} else {
				Pedido pedido = this.consultaPedidoPeloID(id);
				if (pedido == null) {
					System.out.println("Não foi possivel encontrar um pedido com esse ID. Tente novamente.");
				} else {
					if (pedido.getStatus() == StatusPedido.CONCLUIDO) {
						System.out.println("******************************************************************************");
						System.out.println("Esse pedido já se encontra concluido.");
						System.out.println("******************************************************************************");
						System.out.println("Digite qualquer coisa para voltar para a tela de opções de pedido.");
						lerTela(s);
					} else {
						System.out.println("Iniciando conclusão do pedido");
						
						Pedido pedidoConcluido = concluiPedido(pedido);
						System.out.println("******************************************************************************");
						System.out.println("Esse foi o pedido que foi concluido!");
						System.out.println("******************************************************************************");
						System.out.println(pedidoConcluido);
						System.out.println("******************************************************************************");
						System.out.println("Digite qualquer coisa para voltar para a tela de opções de pedido.");
						processa = false;
						lerTela(s);
					}					
				}
			}
		}
	}
	
	private void excluirItemPedido() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		boolean processandoItem = true;
		
		while (processa) {
			this.consultaTodosOsPedidos();
			System.out.println("Selecione o pedido que deseja concluir");
			System.out.println("Se deseja voltar digite 0.");
			String id = lerTela(s);
			
			if (id == null) {
				System.out.println("Informe um pedido");
			} else if (id.equals("0")){
				processa = false;
			} else {
				Pedido pedido = this.consultaPedidoPeloID(id);
				if (pedido == null) {
					System.out.println("Não foi possivel encontrar um pedido com esse ID. Tente novamente.");
				} else {
					if (pedido.getStatus() == StatusPedido.CONCLUIDO) {
						System.out.println("");
						System.out.println("******************************************************************************");
						System.out.println("Esse pedido se encontra concluido. Não é possivel remover um item de um pedido concluido.");
						System.out.println("******************************************************************************");
						System.out.println("Digite qualquer coisa para voltar para a tela de opções de pedido.");
						lerTela(s);
					} else {
						
						while(processandoItem) {
							System.out.println("******************************************************************************");
							for (ItemPedido item  : pedido.getItensPedido()) {
								System.out.println(item);
							}
							System.out.println("Selecione o item a ser removido");
							System.out.println("Se deseja voltar digite 0");
							String idItem = lerTela(s);
							
							if (idItem == null) {
								System.out.println("Informe um item");
							} else if (idItem.equals("0")) {
								processandoItem = false;
								processa = false;
							} else {
								ItemPedido item = pedido.getItensPedido().stream().filter(itemPedido-> itemPedido.getId() == Long.parseLong(idItem)).findAny().orElse(null);
								
								if (item == null) {
									System.out.println("O item não foi encontrado. Tente novamente");
								} else {
									System.out.println("Iniciando a remoção do item do pedido");
									RemoveItemPedido(pedido.getId(), item);
									Pedido pedidoAlterado = consultaPedidoPeloID(String.valueOf(pedido.getId()));
									System.out.println("******************************************************************************");
									System.out.println("O pedido após a remoção do item.");
									System.out.println("******************************************************************************");
									System.out.println(pedidoAlterado);
									System.out.println("******************************************************************************");
									System.out.println("Digite qualquer coisa para voltar para a tela de opções de pedido.");
									processa = false;
									processandoItem = false;
									lerTela(s);
								}
							}
							
							processa = false;
							lerTela(s);	
						}
						
					}					
				}
			}
		}
	}
	
	private Pedido concluiPedido(Pedido pedidoConcluir) {
		pedidoConcluir.setStatus(StatusPedido.CONCLUIDO);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}/concluir";
		
		HttpEntity<Pedido> requestEntity = new HttpEntity<Pedido>(pedidoConcluir, headers);
		ResponseEntity<Pedido> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Pedido.class, pedidoConcluir.getId());
		Pedido pedido = responseEntity.getBody();
		
		return pedido;
	}
	
	private void RemoveItemPedido(Long idPedido ,ItemPedido itemAExcluir) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{idPedido}/item/{idItem}";
		
		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, idPedido, itemAExcluir.getId());
		
	}

}
