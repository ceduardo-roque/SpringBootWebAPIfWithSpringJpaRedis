package br.com.fiap.persistence.simulation.infrastructure;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Secao;

public class LoadDadosLivraria {

	private String URL_FINAL = "secao";
	
	public void loadDadosDaLivraria() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Início da carga de tabela Secoes da Livraria.");
		System.out.println("------------------------------------------------------------------------");

		Map<String, List<String>> secoesECategorias = recuperaMapaDeSecoesECategorias();

		for (String secaoParaAdicionar : secoesECategorias.keySet()) {
			Secao secao = new Secao();
			secao.setDescricao(secaoParaAdicionar);
			String idSecaoCriada = adicionaSecao(secao);
			new LoadCategoriasDeLivrosDasSecoes().carregaCategoriaDaSecao(idSecaoCriada, secoesECategorias.get(secaoParaAdicionar));
		}
	}

	
	private Map<String, List<String>> recuperaMapaDeSecoesECategorias() {
		Map<String, List<String>> secoesECategorias = new HashMap<String, List<String>>();
		secoesECategorias.put("Front-end",Arrays.asList("HTML 5", "CSS", "Bootstrap"));
		secoesECategorias.put("Back-End",Arrays.asList(".Net", "Java", "Node.js", "C#"));
		secoesECategorias.put("Infraestrutura",Arrays.asList("Redes", "Servidores", "Cloud"));
		secoesECategorias.put("Agilidade",Arrays.asList("SCRUM", "XP", "Lean"));
		secoesECategorias.put("Certificações",Arrays.asList("OCP Java", "MCP .Net Framework"));
		secoesECategorias.put("Metodologias",Arrays.asList("RUP", "Agile", "Design Thinking"));
		return secoesECategorias;
	}


	private String adicionaSecao(Secao secao) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = URLPadrao.URLAPIServices + URL_FINAL;

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(secao, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		return path.substring(path.lastIndexOf("/") + 1);
	}
}
