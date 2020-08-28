package br.com.fiap.persistence.tests;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Categoria;

/**
 * Classe de testes da entidade Categoria
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class CategoriaTest {

	public static String urlPadrao = "http://localhost:8080/ecommerce_fiap/rest/categoria";

	public static void main(String args[]) {
		// Teste via chamada JSON na API

		// Teste de adição de categoria
		System.out.println("Adicionando Categoria via JSON...");
		Categoria categoria = new Categoria();
		categoria.setDescricao("Livros de Aventuras");
		int id = Integer.parseInt(CategoriaTest.adicionaCategoria(categoria));
		System.out.println("Categoria adicionada. Id: " + id);
		System.out.println(categoria);
		
		
		System.out.println("Atualizando Categoria de Id: " + id + "...");
		String novaDescricao = "Livros de Literatura Infantil";
		System.out.println("Nova descricão: " + novaDescricao);
		categoria.setId(id);
		categoria.setDescricao(novaDescricao);
		CategoriaTest.atualizaCategoria(categoria);
		System.out.println("Categoria atualizada.");

		System.out.println("Recuperando a categoria de Id: " + id + "...");
		Categoria categoriaRecuperada = CategoriaTest.getCategoriaById(id);
		System.out.println("Categoria recuperada: " + categoriaRecuperada.toString());
		
		// Teste de exclusão da categoria
		System.out.println("Excluindo Categoria de Id: " + id);
		CategoriaTest.excluiCategoria(id);
		System.out.println("Categoria excluída.");

		System.out.println("Listando categorias da tabela Categorias...");
		List<Categoria> listaDeCategorias = CategoriaTest.listaTodasAsCategorias();
		for (Categoria categoriaIterada : listaDeCategorias) {
			System.out.println("Categoria: " + categoriaIterada);
		}
		System.out.println("Fim do Teste de Categoria.");
		adicionaCategoriaNumaSecao();

		
	}

	private static void adicionaCategoriaNumaSecao() {
	
		Categoria categoria1 = new Categoria("Tablets");
		Categoria categoria2 = new Categoria("Livros de Aventura");
		Categoria categoria3 = new Categoria("Livros de Culinaria");
		Categoria categoria4 = new Categoria("Livros Nerds");
		Categoria categoria5 = new Categoria("Livros de Suspense");
		
		try {
			long secao = 118;
			adicionaCategoriaNumaSecao(categoria1, secao);
			adicionaCategoriaNumaSecao(categoria2, secao);
			adicionaCategoriaNumaSecao(categoria3, secao);
			adicionaCategoriaNumaSecao(categoria4, secao);
			adicionaCategoriaNumaSecao(categoria5, secao);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
	}
	
	private static void adicionaCategoriaNumaSecao(Categoria categoria, long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = CategoriaTest.urlPadrao + "/secao/{1}";

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(categoria, headers);
		//URI uri = restTemplate.postForLocation(url, requestEntity, secao);
		restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class, id);
		
	}
	
	private static List<Categoria> listaTodasAsCategorias() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		RestTemplate restTemplate = new RestTemplate();
		String url = CategoriaTest.urlPadrao;
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Categoria[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Categoria[].class);
		
		Categoria[] categorias = responseEntity.getBody();		
		return Arrays.asList(categorias);
	}

	private static Categoria getCategoriaById(int id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = CategoriaTest.urlPadrao + "/{id}";
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Categoria> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Categoria.class, id);
		Categoria categoria = responseEntity.getBody();
		
		return categoria;
	}

	private static String adicionaCategoria(Categoria categoria) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = CategoriaTest.urlPadrao;

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(categoria, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public static void excluiCategoria(long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = CategoriaTest.urlPadrao + "/{id}";

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, id);
	}

	public static void atualizaCategoria(Categoria categoria) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = CategoriaTest.urlPadrao;

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(categoria, headers);
		restTemplate.put(url, requestEntity);
	}



}
