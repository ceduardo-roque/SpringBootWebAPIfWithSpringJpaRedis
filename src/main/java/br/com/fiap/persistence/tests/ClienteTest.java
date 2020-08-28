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

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.EnderecoCliente;
import br.com.fiap.persistence.entity.Secao;
import br.com.fiap.persistence.enums.TipoEndereco;

public class ClienteTest {

	public static String urlPadrao = "http://localhost:8080/ecommerce_fiap/rest/cliente";
	
	
	public static void main(String[] args) {
		
		System.out.println("Cadastrando um cliente");
		
		String id = adicionaCliente1();
		String id2 = adicionaCliente2();
		
		Cliente clienteCadastrado = ClienteTest.getClienteById(Integer.valueOf(id));
		Cliente clienteCadastrado2 = ClienteTest.getClienteById(Integer.valueOf(id2));
		System.out.println("Clientes cadastrados.");
		System.out.println(clienteCadastrado.toString());
		System.out.println(clienteCadastrado2.toString());
		System.out.println("-------------------------------------");
		System.out.println("Listando todos os clientes da base...");
		List<Cliente> allClientes = ClienteTest.getAllClientes();
		for (Cliente cliente : allClientes) {
			System.out.println(cliente);
		}
		ClienteTest.atualizaCliente(clienteCadastrado2);
		ClienteTest.deletaCliente(clienteCadastrado);
	}
	
	
	private static void deletaCliente(Cliente clienteCadastrado) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPadrao + "/{id}";

		HttpEntity<Secao> requestEntity = new HttpEntity<Secao>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, clienteCadastrado.getId());
		
	}


	private static void atualizaCliente(Cliente clienteCadastrado2) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPadrao;

		clienteCadastrado2.setNmCliente("Carlos Eduardo Roque da Silva");
		HttpEntity<Cliente> requestEntity = new HttpEntity<Cliente>(clienteCadastrado2, headers);
		restTemplate.put(url, requestEntity);
		
	}


	private static List<Cliente> getAllClientes() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPadrao;

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Cliente[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Cliente[].class);

		Cliente[] clientes = responseEntity.getBody();
		return Arrays.asList(clientes);
		
	}

	private static String adicionaCliente2() {
		Cliente cli = new Cliente();
		cli.setNmCliente("Maria Isabel Souza");
		cli.setEmail("cliente@cliente.com");
		cli.setNrTelefone(999999);
		EnderecoCliente endCli = new EnderecoCliente();
		
		// Adiciona Dois endereços
		endCli.setLogradouro("Rua das Asturias");
		endCli.setCep("01153011");
		endCli.setNumero(651);
		endCli.setCidade("Jundiai");
		endCli.setComplemento("Casa 120");
		endCli.setTipoEndereco(TipoEndereco.RESIDENCIAL);
		endCli.setEstado("SP");
		
		EnderecoCliente endEntrega = new EnderecoCliente();
		
		endEntrega.setLogradouro("Rua das Mangabeiras");
		endEntrega.setCep("01153013");
		endEntrega.setNumero(650);
		endEntrega.setCidade("Santos");
		endEntrega.setComplemento("Apto");
		endEntrega.setTipoEndereco(TipoEndereco.ENTREGA);
		endEntrega.setEstado("SP");
		
		cli.addEndereco(endCli);
		cli.addEndereco(endEntrega);
		
		String idClienteAdicionado = adicionaCliente(cli);
		System.out.println("Adicionado o Cliente: " + idClienteAdicionado );
		return idClienteAdicionado;
	}

	private static String adicionaCliente1() {
		Cliente cli = new Cliente();
		cli.setNmCliente("Maria Pires");
		cli.setEmail("cliente@cliente.com");
		cli.setNrTelefone(999999);
		EnderecoCliente endCli = new EnderecoCliente();
		
		// Adiciona Dois endereços
		endCli.setLogradouro("Rua das Bandeiras");
		endCli.setCep("01153010");
		endCli.setNumero(650);
		endCli.setCidade("sao paulo");
		endCli.setComplemento("casa");
		endCli.setTipoEndereco(TipoEndereco.RESIDENCIAL);
		endCli.setEstado("SP");
		
		EnderecoCliente endEntrega = new EnderecoCliente();
		
		endEntrega.setLogradouro("Rua das Bandeiras");
		endEntrega.setCep("01153010");
		endEntrega.setNumero(650);
		endEntrega.setCidade("sao paulo");
		endEntrega.setComplemento("casa");
		endEntrega.setTipoEndereco(TipoEndereco.ENTREGA);
		endEntrega.setEstado("SP");
		
		cli.addEndereco(endCli);
		cli.addEndereco(endEntrega);
		
		String idClienteAdicionado = adicionaCliente(cli);
		System.out.println("Adicionado o Cliente: " + idClienteAdicionado );
		return idClienteAdicionado;
	}

	public long incluiCliente() {
		String id = adicionaCliente2();
		return Long.valueOf(id);
	}
	
	private static String adicionaCliente(Cliente cliente) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = ClienteTest.urlPadrao;

		HttpEntity<Cliente> requestEntity = new HttpEntity<Cliente>(cliente, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		return path.substring(path.lastIndexOf("/") + 1);
	}

	private static Cliente getClienteById(int id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = urlPadrao + "/{id}";
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Cliente> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Cliente.class, id);
		Cliente cliente = responseEntity.getBody();
		
		return cliente;
	}
	
	private static List<Cliente> listaClientes() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		RestTemplate restTemplate = new RestTemplate();
		String url = ClienteTest.urlPadrao;
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Cliente[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Cliente[].class);
		
		Cliente[] cliente = responseEntity.getBody();		
		return Arrays.asList(cliente);
	}


}
