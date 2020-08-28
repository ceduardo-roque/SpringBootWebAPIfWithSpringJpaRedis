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

import br.com.fiap.persistence.entity.Cliente;
import br.com.fiap.persistence.entity.EnderecoCliente;
import br.com.fiap.persistence.enums.TipoEndereco;

public class ClienteHelper extends APIHelper {

	private String mensagem;

	public ClienteHelper() {
		super("cliente");
	}

	public void consultaTodosOsClientes() {
		List<Cliente> clientes = listaTodasOsClientes();
		System.out.println("******************************************************************************");
		System.out.println("                         CLIENTES CADASTRADOS");
		System.out.println("******************************************************************************");
		if (clientes != null && !clientes.isEmpty()) {
			for (Cliente cliente : clientes) {
				System.out.println("-------------------------------------------------------------------------------");
				System.out.println(cliente);
				System.out.println("-------------------------------------------------------------------------------");
			}
		} else {
			System.out.println("\nNão foram encontrados Clientes cadastrados.\n");
		}
	}

	private List<Cliente> listaTodasOsClientes() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Cliente[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Cliente[].class);

		Cliente[] clientes = responseEntity.getBody();

		return Arrays.asList(clientes);
	}

	public void cadastraCliente(Cliente novoCliente) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Cliente> requestEntity = new HttpEntity<Cliente>(novoCliente, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);

		String path = uri.getPath();
		String idCriado = path.substring(path.lastIndexOf("/") + 1);
		if (idCriado != null && idCriado.length() > 0)
			System.out.println("Cliente cadastrado com sucesso!");
	}

	public Cliente consultaClientePeloID(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<Cliente> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				Cliente.class, id);
		Cliente cliente = responseEntity.getBody();

		return cliente;
	}

	private void excluiCliente(Cliente cliente) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade() + "/{id}";

		HttpEntity<Cliente> requestEntity = new HttpEntity<Cliente>(headers);
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, cliente.getId());
	}

	public void atualizaCliente(Cliente cliente) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		String url = getCaminhoAPIEntidade();

		HttpEntity<Cliente> requestEntity = new HttpEntity<Cliente>(cliente, headers);
		restTemplate.put(url, requestEntity);
	}

	public void montaMenuCliente(String mensagem) {
		this.mensagem = mensagem;
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println("******************************************************************************");
			System.out.println("        		      	   CLIENTES DA LIVRARIA");
			System.out.println("******************************************************************************");
			System.out.println("\n");
			System.out.println(" 1. Consultar todos os Clientes");
			System.out.println(" 2. Incluir Novo Cliente");
			System.out.println(" 3. Atualizar dados do Cliente");
			System.out.println(" 4. Excluir Cliente");
			System.out.println("\n");
			System.out.println(" 5. Voltar ao MENU Anterior");

			System.out.println("\n");

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);
			if (!valorLido.contentEquals("5"))
				this.trataLeituraTelaCliente(valorLido);
			else
				processa = false;
		}
	}

	private void trataLeituraTelaCliente(String valorLido) {
		switch (valorLido) {
		case "1":
			this.consultaTodosOsClientes();
			break;
		case "2":
			this.cadastraNovoCliente();
			break;
		case "3":
			this.atualizaCliente();
			break;
		case "4":
			this.excluiCliente();
			break;

		default:
			mensagem = "Digite uma opção válida (1, 2, 3, 4)";
		}

	}

	private void excluiCliente() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;

		while (processa) {

			System.out.println(" Escolha o ID de um Cliente para EXCLUIR:");
			System.out.println("\n");
			this.consultaTodosOsClientes();

			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}

			String valorLido = lerTela(s);

			if (valorLido == null || valorLido.length() == 0)
				System.out.println("ID do Cliente Inválido.");
			else {
				Cliente cliente = this.consultaClientePeloID(valorLido);
				if (cliente != null) {
					System.out.println("Deseja realmente excluir o Cliente: " + cliente + " (S/N)");
					valorLido = lerTela(s);
					if (valorLido == null || valorLido.length() == 0) {
						System.out.println("Opção inválida!");
						processa = false;
					} else {
						if (valorLido.equalsIgnoreCase("S")) {
							this.excluiCliente(cliente);
							processa = false;
							System.out.println("Cliente excluído com sucesso!!!");
						}
					}
				} else {
					System.out.println("Cliente com o ID: " + valorLido + " não encontrado.");
					processa = false;
				}
			}
		}

	}

	private void cadastraNovoCliente() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {
			boolean dadosOK = false;

			System.out.println("******************************************************************************");
			System.out.println("                        CADASTRO DE NOVO CLIENTE");
			System.out.println("******************************************************************************");
			System.out.println("Por favor, digite as informações solicitadas:");

			Cliente novoCliente = new Cliente();

			System.out.println("Nome do Cliente: ");
			String nomeCliente = lerTela(s);

			System.out.println("Email:");
			String email = lerTela(s);

			System.out.println("Telefone:");
			String tel = lerTela(s);

			System.out.println("Endereço Residencial:");
			System.out.println("Rua:");
			String nomeRua = lerTela(s);

			System.out.println("Numero:");
			String numero = lerTela(s);

			System.out.println("Complemento:");
			String complemento = lerTela(s);

			System.out.println("Cidade:");
			String cidade = lerTela(s);
			System.out.println("Estado:");
			String estado = lerTela(s);

			System.out.println("CEP:");
			String cep = lerTela(s);

			EnderecoCliente endResidencia = new EnderecoCliente(nomeRua, Integer.valueOf(numero), complemento, cep,
					cidade, estado, TipoEndereco.RESIDENCIAL, null);

			System.out.println("Deseja manter este enderço como endereço de Entrega? (S/N)");
			String resposta = lerTela(s);
			EnderecoCliente endEntrega = null;

			if (resposta.equalsIgnoreCase("N")) {

				System.out.println("Rua:");
				String nomeRuaEntrega = lerTela(s);

				System.out.println("Numero:");
				String numeroEntrega = lerTela(s);

				System.out.println("Complemento:");
				String complementoEntrega = lerTela(s);

				System.out.println("Cidade:");
				String cidadeEntrega = lerTela(s);

				System.out.println("Estado:");
				String estadoEntrega = lerTela(s);

				System.out.println("CEP:");
				String cepEntrega = lerTela(s);
				endEntrega = new EnderecoCliente(nomeRuaEntrega, Integer.valueOf(numeroEntrega), complementoEntrega,
						cepEntrega, cidadeEntrega, estadoEntrega, TipoEndereco.ENTREGA, null);
			} else {
				endEntrega = new EnderecoCliente(nomeRua, Integer.valueOf(numero), complemento, cep, cidade, estado,
						TipoEndereco.ENTREGA, null);
			}

			novoCliente.setNmCliente(nomeCliente);
			novoCliente.setEmail(email);
			novoCliente.addEndereco(endResidencia);
			novoCliente.addEndereco(endEntrega);
			novoCliente.setNrTelefone(Integer.parseInt(tel));

			this.cadastraCliente(novoCliente);
			processa = false;
		}
	}

	private void atualizaCliente() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;

		while (processa) {

			this.consultaTodosOsClientes();
			System.out.println(" Escolha o ID de um Cliente para atualizar:");
			System.out.println("\n");

			String valorLido = lerTela(s);

			if (valorLido == null || valorLido.length() == 0)
				System.out.println("ID do Cliente Inválido.");
			else {
				Cliente cliente = this.consultaClientePeloID(valorLido);
				if (cliente != null) {
					System.out.println("O que deseja atualizar do Cliente?");
					System.out.println("1. Dados do Cliente");
					System.out.println("2. Alterar endereço do Cliente");
					System.out.println("3. Alterar endereço de entrega do Cliente");
					valorLido = lerTela(s);
					try {
						if (valorLido == null || valorLido.length() == 0) {
							System.out.println("Novo nome inválido!");
							processa = false;
						} else if (valorLido.equals("1")) {
							System.out.println("Nome do Cliente: ");
							String nomeCliente = lerTela(s);

							System.out.println("Email:");
							String email = lerTela(s);

							System.out.println("Telefone:");
							String tel = lerTela(s);
							cliente.setNmCliente(nomeCliente);
							cliente.setEmail(email);
							cliente.setNrTelefone(Integer.valueOf(tel));
							this.atualizaCliente(cliente);
							processa = false;
							System.out.println("\nCliente atualizado com sucesso!!!\n");
						} else if (valorLido.equals("2") || valorLido.equals("3")) {
							System.out.println("Digite os valores do novo endereço:");
							System.out.println("Rua:");
							String nomeRua = lerTela(s);

							System.out.println("Numero:");
							String numero = lerTela(s);

							System.out.println("Complemento:");
							String complemento = lerTela(s);

							System.out.println("Cidade:");
							String cidade = lerTela(s);

							System.out.println("Estado:");
							String estado = lerTela(s);

							System.out.println("CEP:");
							String cep = lerTela(s);

							for (EnderecoCliente endereco : cliente.getEnderecos()) {
								if (valorLido.equals("2")
										&& endereco.getTipoEndereco().equals(TipoEndereco.RESIDENCIAL)) {
									endereco.setLogradouro(nomeRua);
									endereco.setNumero(Integer.valueOf(numero));
									endereco.setComplemento(complemento);
									endereco.setCep(cep);
									endereco.setCidade(cidade);
									endereco.setEstado(estado);
								} else if (valorLido.equals("3")
										&& endereco.getTipoEndereco().equals(TipoEndereco.ENTREGA)) {
									endereco.setLogradouro(nomeRua);
									endereco.setNumero(Integer.valueOf(numero));
									endereco.setComplemento(complemento);
									endereco.setCep(cep);
									endereco.setCidade(cidade);
									endereco.setEstado(estado);
								}
							}
							this.atualizaCliente(cliente);
							processa = false;
							System.out.println("\nCliente atualizado com sucesso!!!\n");
						} else {
							System.out.println("Cliente de ID " + valorLido + " não encontrado!");
							processa = false;
						}
					} catch (NumberFormatException e) {
						System.out.println("\nDados do cliente incorretos. Por favor, tente novamente.\n");
					}

				}
			}
		}
	}
}
