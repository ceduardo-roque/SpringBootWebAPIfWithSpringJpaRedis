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
import br.com.fiap.persistence.entity.Secao;

/**
 * Classe de testes da entidade Secao e categorias de Secao
 *
 */
public class SecaoTest {

	public static String urlPadrao = "http://localhost:8080/ecommerce_fiap/rest/secao";

	public static void main(String args[]) {

		// Teste via chamada JSON na API

		//incluiEManipulaSecaoSemCategorias();
		incluiEManipulaSecaoComCategorias();
		

	}

	private static void incluiEManipulaSecaoComCategorias() {
		System.out.println("\n ------------------------------------------------------------------------");
		System.out.println("\n Início do Teste de CRUD de Secao com categorias.");
		System.out.println("\n ------------------------------------------------------------------------");
		// Teste de adição de secao
		System.out.println("Adicionando Secao via JSON...");
		Secao secao = new Secao();
		secao.setDescricao("Eletronicos");
		int id = Integer.parseInt(SecaoTest.adicionaSecao(secao));
		secao.setId(id);
		System.out.println("Seção adicionada. Iniciando adição de Categorias.");
		
		
		System.out.println("\n ------------------------------------------------------------------------");
		
//		System.out.println("Atualizando Categorias da Secao de Id: " + id + "...");
//		System.out.println("Removendo uma categoria");
//		System.out.println("Buscando categorias da secao de Id: " + id);
//		
//		List<Categoria> listCategoria = SecaoTest.recuperaCategoriasDaSecao(id);
//		for (Categoria categoria : listCategoria) {
//			System.out.println(categoria);
//		}
//		
		
//		SecaoTest.atualizaSecao(secao);
//		System.out.println("Categorias após remoção..."); secao.imprimeCategorias();
//		Secao secaoRecuperada = SecaoTest.getSecaoById(id);
//		System.out.println("Categorias da Secao recuperada: "); secaoRecuperada.imprimeCategorias();
//		
//		System.out.println("\n --------------------------------------------------------");
//		
//		
//		System.out.println("Recuperando a secao de Id: " + id + "...");
//		Secao secaoBuscada = SecaoTest.getSecaoById(id);
//		System.out.println("Secao recuperada: " + secaoBuscada.toString());
//		
//		System.out.println("\n ------------------------------------------------------------------------");
//		
		/*
		 * // Teste de exclusão da secao System.out.println("Excluindo Secao de Id: " +
		 * id); SecaoTest.excluiSecao(id); System.out.println("Secao excluída.");
		 * 
		 * // Testar se a exclusão é em cascata
		 * 
		 */

		System.out.println("Listando seções da tabela Secao...");
		List<Secao> listaDeSecoes = SecaoTest.listaTodasAsSecoes();
		for (Secao secaoIterada : listaDeSecoes) {
			System.out.println("Secao: " + secaoIterada);
		}
		System.out.println("Fim do Teste de Secao.");

		System.out.println("\n Finalização do Teste de CRUD de Secao sem categorias.");

	}

	private static List<Categoria> recuperaCategoriasDaSecao(int id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao + "/categorias/{1}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Categoria[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Categoria[].class, id);

		Categoria[] secoes = responseEntity.getBody();
		return Arrays.asList(secoes);

		
		
	}

	private static void incluiEManipulaSecaoSemCategorias() {
		System.out.println("\n ------------------------------------------------------------------------");
		System.out.println("\n Início do Teste de CRUD de Secao sem categorias.");
		System.out.println("\n ------------------------------------------------------------------------");
		// Teste de adição de secao
		System.out.println("Adicionando Secao via JSON...");
		Secao secao = new Secao();
		secao.setDescricao("Livraria");
		int id = Integer.parseInt(SecaoTest.adicionaSecao(secao));
		System.out.println("Secao adicionada. Id: " + id);
		System.out.println(secao);

		System.out.println("Atualizando Secao de Id: " + id + "...");
		String novaDescricao = "Papelaria";
		System.out.println("Nova descricão: " + novaDescricao);
		secao.setId(id);
		secao.setDescricao(novaDescricao);
		SecaoTest.atualizaSecao(secao);
		System.out.println("Secao atualizada.");

		System.out.println("Recuperando a secao de Id: " + id + "...");
		Secao secaoRecuperada = SecaoTest.getSecaoById(id);
		System.out.println("Secao recuperada: " + secaoRecuperada.toString());

		// Teste de exclusão da secao
		System.out.println("Excluindo Secao de Id: " + id);
		SecaoTest.excluiSecao(id);
		System.out.println("Secao excluída.");

		System.out.println("Listando seções da tabela Secao...");
		List<Secao> listaDeSecaos = SecaoTest.listaTodasAsSecoes();
		for (Secao secaoIterada : listaDeSecaos) {
			System.out.println("Secao: " + secaoIterada);
		}
		System.out.println("Fim do Teste de Secao.");

		System.out.println("\n Finalização do Teste de CRUD de Secao sem categorias.");

	}

	private static void adicionaCategoriaNaSecao(Secao secao) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao + "/categoria/add/{1}";

//		Set<Categoria> categorias = secao.getCategorias();
//		for (Categoria categoria : categorias) {
			HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(secao, headers);
			restTemplate.postForLocation(url, requestEntity);
//		}

	}
	
	private static List<Secao> listaTodasAsSecoes() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao;

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Secao[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Secao[].class);

		Secao[] secoes = responseEntity.getBody();
		return Arrays.asList(secoes);
	}

	private static Secao getSecaoById(int id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Secao> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Secao.class,
				id);
		Secao secao = responseEntity.getBody();

		return secao;
	}

	private static String adicionaSecao(Secao secao) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao;

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(secao, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public static void excluiSecao(long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao + "/{id}";

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, id);
	}

	public static void atualizaSecao(Secao secao) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = SecaoTest.urlPadrao;

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(secao, headers);
		restTemplate.put(url, requestEntity);
	}

}
