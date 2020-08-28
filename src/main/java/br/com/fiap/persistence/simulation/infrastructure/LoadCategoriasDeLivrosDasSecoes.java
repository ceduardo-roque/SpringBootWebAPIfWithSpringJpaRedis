package br.com.fiap.persistence.simulation.infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Categoria;
import br.com.fiap.persistence.entity.Produto;

public class LoadCategoriasDeLivrosDasSecoes {

	private String URL_FINAL = "categoria";

	public void carregaCategoriaDaSecao(String idSecao, List<String> categorias) {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Início da carga de tabela Categorias da Livraria");
		System.out.println("------------------------------------------------------------------------");

		for (String categoria : categorias) {
			Categoria categoriaParaAdicionar = new Categoria();
			categoriaParaAdicionar.setDescricao(categoria);
			adicionaCategoriaNumaSecao(categoriaParaAdicionar, Long.valueOf(idSecao));
			adicionaProdutosDaCategoria(recuperaListaDeProdutosCategoria(categoriaParaAdicionar));
		}
	}
	
	private void adicionaProdutosDaCategoria(List<Produto> listaDeProdutos) {
		new LoadProdutos().carregaProdutosDaLivraria(listaDeProdutos);
	}

	private List<Produto> recuperaListaDeProdutosCategoria(Categoria categoriaParaAdicionar) {
		List<Produto> produtos = new ArrayList<Produto>();
		switch (categoriaParaAdicionar.getDescricao()) {
			case "HTML 5":
				produtos = getProdutosHtml(categoriaParaAdicionar);
				break;
			case "CSS":
				produtos = getProdutosCSS(categoriaParaAdicionar);
				break;
			case "Bootstrap":
				produtos = getProdutosBootstrap(categoriaParaAdicionar);
				break;
			case ".Net":
				produtos = getProdutosNet(categoriaParaAdicionar);
				break;
			case "Java":
				produtos = getProdutosJava(categoriaParaAdicionar);
				break;
			case "Node.js":
				produtos = getProdutosNode(categoriaParaAdicionar);
				break;
			case "C#":
				produtos = getProdutosCSharp(categoriaParaAdicionar);
				break;
			case "Redes":
				produtos = getProdutosRedes(categoriaParaAdicionar);
				break;
			case "Servidores":
				produtos = getProdutosServidores(categoriaParaAdicionar);
				break;
			case "Cloud":
				produtos = getProdutosCloud(categoriaParaAdicionar);
				break;
			case "SCRUM":
				produtos = getProdutosScrum(categoriaParaAdicionar);
				break;
	
			case "XP":
				produtos = getProdutosXP(categoriaParaAdicionar);
				break;
	
			case "Lean":
				produtos = getProdutosLean(categoriaParaAdicionar);
				break;
	
			case "OCP Java":
				produtos = getProdutosOCP(categoriaParaAdicionar);
				break;
	
			case "MCP .Net Framework":
				produtos = getProdutosMCP(categoriaParaAdicionar);
				break;
	
			case "RUP":
				produtos = getProdutosRUP(categoriaParaAdicionar);
				break;
	
			case "Agile":
				produtos = getProdutosAgile(categoriaParaAdicionar);
				break;
			case "Design Thinking":
				produtos = getProdutosDesignThinking(categoriaParaAdicionar);
				break;
		}

		return produtos;
	}

	private List<Produto> getProdutosDesignThinking(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Design Thinking na veia", categoriaParaAdicionar, 65.0, 16));
	}

	private List<Produto> getProdutosAgile(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Agile na veia", categoriaParaAdicionar, 7.0, 10));
	}

	private List<Produto> getProdutosRUP(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("RUP na veia", categoriaParaAdicionar, 45.0, 27));
	}

	private List<Produto> getProdutosMCP(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Certificacao MCP .Net na veia", categoriaParaAdicionar, 750.0, 50));
	}

	private List<Produto> getProdutosOCP(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Cetificacao Java OCP na veia", categoriaParaAdicionar, 650.0, 50));
	}

	private List<Produto> getProdutosLean(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Lean na veia", categoriaParaAdicionar, 100.0, 60));
	}

	private List<Produto> getProdutosXP(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("XP na veia", categoriaParaAdicionar, 20.0, 28));
	}

	private List<Produto> getProdutosScrum(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("SCRUM na veia", categoriaParaAdicionar, 250.0, 70));
	}

	private List<Produto> getProdutosCloud(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Cloud na veia", categoriaParaAdicionar, 80.0, 60));
	}

	private List<Produto> getProdutosServidores(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Servers na veia", categoriaParaAdicionar, 1500.0, 2));
	}

	private List<Produto> getProdutosRedes(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Redes na veia", categoriaParaAdicionar, 130.0, 50));
	}

	private List<Produto> getProdutosCSharp(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("C# na veia", categoriaParaAdicionar, 40.0, 20));
	}

	private Produto criaProduto(String nome, Categoria categoriaParaAdicionar, double valor, int estoque) {
		Produto prod1 = new Produto();
		prod1.setDescricao(nome);
		prod1.setCategoria(categoriaParaAdicionar);
		prod1.setPreco(valor);
		prod1.setQuantidadeEstoque(estoque);
		return prod1;
	}

	private List<Produto> getProdutosNode(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Node.js na veia", categoriaParaAdicionar, 150.0, 20));
	}

	private List<Produto> getProdutosJava(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Java na veia", categoriaParaAdicionar, 50.0, 30));
	}

	private List<Produto> getProdutosNet(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto(".Net na veia", categoriaParaAdicionar, 40.0, 25));
	}

	private List<Produto> getProdutosBootstrap(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("Bootstrap na veia", categoriaParaAdicionar, 30.0, 30));
	}

	private List<Produto> getProdutosCSS(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("CSS na veia", categoriaParaAdicionar, 20.0, 20));
	}

	private List<Produto> getProdutosHtml(Categoria categoriaParaAdicionar) {
		return Arrays.asList(criaProduto("HTML 5 na veia", categoriaParaAdicionar, 10.0, 10));
	}

	private void adicionaCategoriaNumaSecao(Categoria categoria, long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = URLPadrao.URLAPIServices + URL_FINAL + "/secao/{1}";

		HttpEntity<Categoria> requestEntity = new HttpEntity<Categoria>(categoria, headers);
		// URI uri = restTemplate.postForLocation(url, requestEntity, secao);
		restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class, id);
	}
}
