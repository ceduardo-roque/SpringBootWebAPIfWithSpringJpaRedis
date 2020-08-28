package br.com.fiap.persistence.tests;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Categoria;
import br.com.fiap.persistence.entity.Produto;

public class ProdutoTest {
	public static String urlPadrao = "http://localhost:8080/ecommerce_fiap/rest/produtos";


	public static void main(String args[]) {

		// Teste via chamada JSON na API

		// incluiEManipulaSecaoSemCategorias();
		DeveInserirUmProduto();

	}

	public Produto recuperaProduto(int idProduto) {
		return getProdutoById(idProduto);
	}
	
	private static String adiciona(Produto produto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPadrao;

		HttpEntity<Produto> requestEntity = new HttpEntity<Produto>(produto, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		return path.substring(path.lastIndexOf("/") + 1);
	}

	private static void DeveInserirUmProduto() {
		System.out.println("\n ------------------------------------------------------------------------");
		System.out.println("\n Início do Teste de CRUD de Produto com categorias.");
		System.out.println("\n ------------------------------------------------------------------------");
		// Teste de adição de secao
		System.out.println("Adicionando Produto via JSON...");
		Produto produto = new Produto();
		produto.setPreco(200.00);
		produto.setDescricao("produto de teste");
		produto.setQuantidadeEstoque(45);
		Categoria cat = new Categoria();
		cat.setId(1);
		produto.setCategoria(cat);
		int id = Integer.parseInt(adiciona(produto));

		System.out.println("Produto adicionado.");
		Produto produto2 = getProdutoById(id);
		System.out.println(produto2.toString());
		System.out.println("\n ------------------------------------------------------------------------");
	}

	private static Produto getProdutoById(int id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPadrao + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Produto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Produto.class, id);
		Produto secao = responseEntity.getBody();

		return secao;
	}
}