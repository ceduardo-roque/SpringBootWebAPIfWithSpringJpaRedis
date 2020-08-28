package br.com.fiap.persistence.simulation.infrastructure;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Produto;

public class LoadProdutos {

	private String URL_FINAL = "produtos";
	
	public void carregaProdutosDaLivraria(List<Produto> listaDeProdutos) {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Início da carga de tabela Produtos da Livraria (Livros)");
		System.out.println("------------------------------------------------------------------------");
		for (Produto produto : listaDeProdutos) {
			adicionaProduto(produto);
		}
	}

	private void adicionaProduto(Produto produto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = URLPadrao.URLAPIServices + URL_FINAL;

		HttpEntity<Produto> requestEntity = new HttpEntity<Produto>(produto, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);
	}

}
