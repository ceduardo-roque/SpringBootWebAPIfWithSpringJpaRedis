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

public class CategoriaHelper extends APIHelper {

	private String mensagem;

	public CategoriaHelper() {
		super("categoria");
	}

	public void consultaTodasAsCategorias() {
		List<Categoria> categorias = listaTodasAsCategorias();
		System.out.println("******************************************************************************");
		System.out.println("                           Categorias Cadastradas");
		System.out.println("******************************************************************************");

		for (Categoria categoria : categorias) {
			System.out.println("      " + categoria);
		}
	}

	private List<Categoria> listaTodasAsCategorias() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Categoria[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Categoria[].class);

		Categoria[] categorias = responseEntity.getBody();
		if (categorias == null || categorias.length == 0)
			System.out.println("Não foram encontradas Categorias cadastradas.");
		return Arrays.asList(categorias);
	}

	public void cadastraCategoria(String nomeCategoria) {
		Categoria novaCategoria = new Categoria();
		novaCategoria.setDescricao(nomeCategoria);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(novaCategoria, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		String idCriado = path.substring(path.lastIndexOf("/") + 1);
		if (idCriado != null && idCriado.length() > 0)
			System.out.println("Categoria Criada com sucesso!");
	}

	public Categoria consultaCategoriaPeloID(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Categoria> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Categoria.class,
				id);
		Categoria categoria = responseEntity.getBody();

		return categoria;

	}

	private void excluiCategoria(Categoria categoria) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, categoria.getId());
	}
	
	public void atualizaCategoria(Categoria categoria) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(categoria, headers);
		restTemplate.put(url, requestEntity);
		
	}
	
	public void montaMenuCategorias(String mensagem) {
		this.mensagem = mensagem;
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println("******************************************************************************");
			System.out.println("        		      CATEGORIAS DE LIVROS DA LIVRARIA");
			System.out.println("******************************************************************************");
			System.out.println("\n");
			System.out.println(" 1. Listar Categorias");
			System.out.println(" 2. Incluir Nova Categoria");
			System.out.println(" 3. Atualizar uma Categoria");
			System.out.println(" 4. Excluir uma Categoria");
			System.out.println("\n");
			System.out.println(" 5. Voltar ao MENU Anterior");

			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			if(!valorLido.contentEquals("5"))
				this.trataLeituraTelaCategoria(valorLido);
			else
				processa = false;
		}
	}

	private void trataLeituraTelaCategoria(String valorLido) {
		switch (valorLido) {
		case "1":
			this.consultaTodasAsCategorias();
			break;
		case "2":
			this.cadastraNovaCategoria();
			break;
		case "3":
			this.atualizaCategoria();
			break;
		case "4":
			this.excluiCategoria();
			break;
		
		default:
			mensagem = "Digite uma opção válida (1, 2, 3, 4)";
		}

	}


	private void excluiCategoria() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println(" Escolha o ID de uma SECAO para EXCLUIR:");
			System.out.println("\n");
			this.consultaTodasAsCategorias();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if(valorLido==null || valorLido.length()==0) 
				System.out.println("ID da Secao Inválido.");
			else {
				Categoria categoria = this.consultaCategoriaPeloID(valorLido);
				if(categoria!=null) {
					System.out.println("Deseja realmente excluir a Categoria: " + categoria + " (S/N)");
					valorLido = lerTela(s);
					if(valorLido==null || valorLido.length()==0) {
						System.out.println("Opção inválida!");
						processa = false;
					} else {
						if(valorLido.equals("S")) {
							this.excluiCategoria(categoria);
							processa = false;
							System.out.println("Categoria excluída com sucesso!!!");
						}
					}
				} else {
					System.out.println("Categoria com o ID: " + valorLido + " não encontrada.");
					processa = false;
				}
			}
		}
		
	}

	private void cadastraNovaCategoria() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println(" Digite o nome da NOVA CATEGORIA");
			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			processa = false;
			if(valorLido==null || valorLido.length()==0) 
				System.out.println("Nome de Categoria inválido.");
			else {
				this.cadastraCategoria(valorLido);
			}
		}
	}

	private void atualizaCategoria() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println(" Escolha o ID de uma CATEGORIA para atualizar:");
			System.out.println("\n");
			this.consultaTodasAsCategorias();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if(valorLido==null || valorLido.length()==0) 
				System.out.println("ID da Categoria Inválido.");
			else {
				Categoria categoria = this.consultaCategoriaPeloID(valorLido);
				if(categoria!=null) {
					System.out.println("Digite o novo nome para a Categoria:");
					valorLido = lerTela(s);
					if(valorLido==null || valorLido.length()==0) {
						System.out.println("Novo nome inválido!");
						processa = false;
					} else {
						categoria.setDescricao(valorLido);
						this.atualizaCategoria(categoria);
						processa = false;
						System.out.println("Categoria atualizada com sucesso!");
					}
				} else {
					System.out.println("Categoria de ID: " + valorLido + " não encontrada.");
				}
			}
		}
	}

}
