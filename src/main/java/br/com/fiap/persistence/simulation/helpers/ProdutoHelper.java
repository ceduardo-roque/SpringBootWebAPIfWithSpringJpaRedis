package br.com.fiap.persistence.simulation.helpers;

import java.net.URI;
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
import br.com.fiap.persistence.entity.Produto;

public class ProdutoHelper extends APIHelper {

	private String mensagem;

	public ProdutoHelper() {
		super("produtos");
	}

	public void consultaTodosOsProdutos() {
		List<Produto> produtos = listaTodasOsLivros();
		
		System.out.println("******************************************************************************");
		System.out.println("                           PRODUTOS CADASTRADOS");
		System.out.println("******************************************************************************");
		if(!produtos.isEmpty()) {
			for (Produto produto : produtos) {
				System.out.println("      " + produto);
			}
			System.out.println("\n\n");
		}
		else
			System.out.println("Não foram encontrados Produtos cadastrados.\n\n");
	}

	private List<Produto> listaTodasOsLivros() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Produto[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Produto[].class);

		Produto[] produtos = responseEntity.getBody();
		return Arrays.asList(produtos);
	}

	public void cadastraProduto(String nomeProduto, String valorLidoPreco, String valorLidoQuantidade,
			Categoria categoria) {
		Produto novoProduto = new Produto();
		novoProduto.setDescricao(nomeProduto);
		novoProduto.setPreco(Double.valueOf(valorLidoPreco));
		novoProduto.setQuantidadeEstoque(Integer.valueOf(valorLidoQuantidade));
		novoProduto.setCategoria(categoria);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Produto> requestEntity = new HttpEntity<Produto>(novoProduto, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		String idCriado = path.substring(path.lastIndexOf("/") + 1);
		if (idCriado != null && idCriado.length() > 0)
			System.out.println("Produto Criado com sucesso!");
	}

	public Produto consultaProdutoPeloID(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Produto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Produto.class, id);
		Produto produto = responseEntity.getBody();

		return produto;

	}

	private void excluiProduto(Produto produto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<Produto> requestEntity = new HttpEntity<Produto>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, produto.getId());
	}

	public void atualizaProduto(Produto produto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Produto> requestEntity = new HttpEntity<Produto>(produto, headers);
		restTemplate.put(url, requestEntity);

	}

	public void montaMenuProdutos(String mensagem) {
		this.mensagem = mensagem;
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println("******************************************************************************");
			System.out.println("        		PRODUTOS/LIVROS DA LIVRARIA");
			System.out.println("******************************************************************************");
			System.out.println("\n");
			System.out.println(" 1. Consultar Livro/Produto");
			System.out.println(" 2. Incluir Novo Livro/Produto");
			System.out.println(" 3. Atualizar um Livro/Produto");
			System.out.println(" 4. Excluir um Livro/Produto");
			System.out.println("\n");
			System.out.println(" 5. Voltar ao MENU Anterior");

			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			if (!valorLido.contentEquals("5"))
				this.trataLeituraTelaLivros(valorLido);
			else
				processa = false;
		}
	}

	private void trataLeituraTelaLivros(String valorLido) {
		switch (valorLido) {
		case "1":
			this.consultaTodosOsProdutos();
			break;
		case "2":
			this.cadastraNovoProduto();
			break;
		case "3":
			this.atualizaProduto();
			break;
		case "4":
			this.excluiProduto();
			break;

		default:
			mensagem = "Digite uma opção válida (1, 2, 3, 4)";
		}

	}

	private void excluiProduto() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;

		while (processa) {

			System.out.println(" Escolha o ID de um Produto para EXCLUIR:");
			System.out.println("\n");
			this.consultaTodosOsProdutos();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if (valorLido == null || valorLido.length() == 0)
				System.out.println("ID do Produto Inválido.");
			else {
				Produto produto = this.consultaProdutoPeloID(valorLido);
				if (produto != null) {
					System.out.println("Deseja realmente excluir o Produto: " + produto + " (S/N)");
					valorLido = lerTela(s);
					if (valorLido == null || valorLido.length() == 0) {
						System.out.println("Opção inválida!");
						processa = false;
					} else {
						if (valorLido.equalsIgnoreCase("S")) {
							this.excluiProduto(produto);
							processa = false;
							System.out.println("Produto excluído com sucesso!!!");
						}
					}
				} else {
					System.out.println("Produto com o ID: " + valorLido + " não encontrado.");
					processa = false;
				}
			}
		}

	}

	private void cadastraNovoProduto() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {
			boolean dadosOK = false;

			Categoria categoria = escolheCategoriaParaOProduto();

			if (categoria != null) {
				// Nome do produto
				System.out.println(" Digite o NOME do NOVO PRODUTO");
				System.out.println("\n");
				String valorLidoNome = lerTela(s);
				String valorLidoPreco = "";
				String valorLidoQuantidade = "";

				if (valorLidoNome == null || valorLidoNome.length() == 0)
					System.out.println("Nome de Produto inválido.");
				else {
					System.out.println(" Digite o PREÇO do NOVO PRODUTO");
					System.out.println("\n");
					valorLidoPreco = lerTela(s);
					if (valorLidoPreco == null || valorLidoPreco.length() == 0)
						System.out.println("Preço de Produto inválido.");
					else {
						System.out.println(" Digite o QUANTIDADE EM ESTOQUE do NOVO PRODUTO");
						System.out.println("\n");
						valorLidoQuantidade = lerTela(s);
						if (valorLidoPreco == null || valorLidoPreco.length() == 0) {
							System.out.println("Quantidade de Produto em estoque inválido.");
						} else {
							dadosOK = true;
						}
					}
				}

				if (dadosOK) {
					this.cadastraProduto(valorLidoNome, valorLidoPreco, valorLidoQuantidade, categoria);
				} else {
					System.out.println("Dados do Novo Produto insuficientes.");
				}
				processa = false;

			} else {
				System.out.println("Por favor recomece e escolha uma Categoria válida.");
				processa = false;
			}

		}
	}

	private Categoria escolheCategoriaParaOProduto() {
		Scanner s = new Scanner(System.in);
		CategoriaHelper helper = new CategoriaHelper();
		System.out.println("Escolha uma Categoria para o Produto: ");
		helper.consultaTodasAsCategorias();

		String valorLido = lerTela(s);

		if (valorLido == null || valorLido.length() == 0) {
			System.out.println("ID da Categoria Inválido.");
			return null;
		} else {
			Categoria categoria = helper.consultaCategoriaPeloID(valorLido);
			if (categoria != null) {
				System.out.println("Deseja realmente escolher a Categoria: " + categoria + " (S/N)");
				valorLido = lerTela(s);
				if (valorLido == null || valorLido.length() == 0) {
					System.out.println("Opção inválida!");
				} else {
					if (valorLido.equalsIgnoreCase("S")) {
						return categoria;
					} else
						return null;
				}
			} else {
				System.out.println("Categoria com o ID: " + valorLido + " não encontrada.");
				return null;
			}
		}

		return null;
	}

	private void atualizaProduto() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;

		while (processa) {

			System.out.println(" Escolha o ID de um Produto para atualizar:");
			System.out.println("\n");
			this.consultaTodosOsProdutos();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String id = lerTela(s);

			if (id == null || id.length() == 0)
				System.out.println("ID do Produto Inválido.");
			else {
				Produto produto = this.consultaProdutoPeloID(id);
				if (produto != null) {
					System.out.println("Digite o novo nome para o Produto:");
					String novoNome = lerTela(s);
					System.out.println("Digite o novo preço do Produto:");
					String novoPreco = lerTela(s);
					System.out.println("Digite a nova quantidade do estoque do Produto:");
					String novaQtdeEstoque = lerTela(s);
					if (novoNome == null || novoNome.length() == 0 || novoPreco == null || novoPreco.length() ==0 || novaQtdeEstoque == null || novaQtdeEstoque.length()==0) {
						System.out.println("Dados do Produto inválido! Favor digitar os valores corretamente.");
						processa = true;
					} else {
						try {
							produto.setDescricao(novoNome);
							produto.setPreco(Double.valueOf(novoPreco));
							produto.setQuantidadeEstoque(Integer.valueOf(novaQtdeEstoque));
							this.atualizaProduto(produto);
							processa = false;	
							System.out.println("\nProduto atualizado com sucesso!\n");
						} catch (Exception e) {
							System.out.println("\nDados do Produto inválido! Favor digitar os valores corretamente.\n");
						}
					}
				} else {
					System.out.println("Produto de ID " + id + " não encontrado!");
					processa = false;
				}
			}
		}
	}

	public void imprimeEstoqueDeProdutos(String mensagem2) {
		System.out.println("******************************************************************************");
		System.out.println("                    ESTOQUE DE PRODUTOS CADASTRADOS");
		System.out.println("******************************************************************************");

		for (Produto produto : listaTodasOsLivros()) {
			System.out.println("   ID            : " + produto.getId());
			System.out.println("   Descrição     : " + produto.getDescricao());
			System.out.println("   Total Estoque : " + produto.getQuantidadeEstoque());
			System.out.println("-------------------------------------------------------------------------------");

		}

	}

}
