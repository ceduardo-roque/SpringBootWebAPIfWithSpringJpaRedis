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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.persistence.entity.Secao;

public class SecaoHelper extends APIHelper {

	private String mensagem;

	public SecaoHelper() {
		super("secao");
	}

	public void consultaTodasAsSecoes() {
		List<Secao> secoes = listaTodasAsSecoes();
		System.out.println("******************************************************************************");
		System.out.println("                           SEÇÕES CADASTRADAS");
		System.out.println("******************************************************************************");
		for (Secao secao : secoes) {
			System.out.println("      " + secao);
		}
		System.out.println("******************************************************************************");
		System.out.println("\n\n");
	}

	private List<Secao> listaTodasAsSecoes() {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			RestTemplate restTemplate = new RestTemplate();
			String url = getCaminhoAPIEntidade();

			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			ResponseEntity<Secao[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					Secao[].class);

			Secao[] secoes = responseEntity.getBody();
			if (secoes == null || secoes.length == 0)
				System.out.println("Não foram encontradas Seções cadastradas.");
			return Arrays.asList(secoes);
		} catch(ResourceAccessException e) {
			throw e;
		}
	}

	public void cadastraSecao(String nomeSecao) {
		Secao novaSecao = new Secao();
		novaSecao.setDescricao(nomeSecao);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(novaSecao, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		String idCriado = path.substring(path.lastIndexOf("/") + 1);
		if (idCriado != null && idCriado.length() > 0)
			System.out.println("Secao Criada com sucesso!");
	}

	public Secao consultaSecaoPeloID(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Secao> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Secao.class,
				id);
		Secao secao = responseEntity.getBody();

		return secao;

	}

	private void excluiSecao(Secao secao) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, secao.getId());
	}
	
	public void atualizaSecao(Secao secao) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(secao, headers);
		restTemplate.put(url, requestEntity);
		
	}
	
	public void montaMenuSecoes(String mensagem) {
		this.mensagem = mensagem;
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println("******************************************************************************");
			System.out.println("        		    SEÇÕES DA LIVRARIA");
			System.out.println("******************************************************************************");
			System.out.println("\n");
			System.out.println(" 1. Listar Seções");
			System.out.println(" 2. Incluir Nova Seção");
			System.out.println(" 3. Atualizar uma Seção");
			System.out.println(" 4. Excluir uma Seção");
			System.out.println("\n");
			System.out.println(" 5. Voltar ao MENU Anterior");

			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			if(!valorLido.contentEquals("5"))
				this.trataLeituraTelaSecao(valorLido);
			else
				processa = false;
		}
	}

	private void trataLeituraTelaSecao(String valorLido) {
		switch (valorLido) {
		case "1":
			this.consultaTodasAsSecoes();
			break;
		case "2":
			this.cadastraNovaSecao();
			break;
		case "3":
			this.atualizaSecao();
			break;
		case "4":
			this.excluiSecao();
			break;
		
		default:
			mensagem = "Digite uma opção válida (1, 2, 3, 4)";
		}

	}


	private void excluiSecao() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
	
		while (processa) {

			System.out.println(" Escolha o ID de uma SECAO para EXCLUIR:");
			System.out.println("\n");
			this.consultaTodasAsSecoes();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if(valorLido==null || valorLido.length()==0) 
				System.out.println("ID da Secao Inválido.");
			else {
				Secao secao = this.consultaSecaoPeloID(valorLido);
				if(secao!=null) {
					System.out.println("Deseja realmente excluir a Seção: " + secao + " (S/N)");
					valorLido = lerTela(s);
					if(valorLido==null || valorLido.length()==0) {
						System.out.println("Opção inválida!");
						processa = false;
					} else {
						if(valorLido.equals("S")) {
							this.excluiSecao(secao);
							processa = false;
							System.out.println("Seção excluída com sucesso!!!");
						}
					}
				} else {
					System.out.println("Seção com o ID: " + valorLido + " não encontrada.");
					processa = false;
				}
			}
		}
		
	}

	private void cadastraNovaSecao() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println(" Digite o nome da NOVA SEÇÃO");
			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			processa = false;
			if(valorLido==null || valorLido.length()==0) 
				System.out.println("Nome de seção inválida.");
			else {
				
				this.cadastraSecao(valorLido);
			}
		}
	}

	private void atualizaSecao() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		
		while (processa) {

			System.out.println(" Escolha o ID de uma SECAO para atualizar:");
			System.out.println("\n");
			this.consultaTodasAsSecoes();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if(valorLido==null || valorLido.length()==0) 
				System.out.println("ID da Secao Inválido.");
			else {
				Secao secao = this.consultaSecaoPeloID(valorLido);
				if(secao!=null) {
					System.out.println("Digite o novo nome para a Seção:");
					valorLido = lerTela(s);
					if(valorLido==null || valorLido.length()==0) {
						System.out.println("Novo nome inválido!");
						processa = false;
					} else {
						secao.setDescricao(valorLido);
						this.atualizaSecao(secao);
						processa = false;
					}
				} else {
					System.out.println("Secao de ID " + valorLido + " não encontrada!");
					processa = false;
				}
			}
		}
	}

}
