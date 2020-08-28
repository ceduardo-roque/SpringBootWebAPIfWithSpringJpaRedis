package br.com.fiap.persistence.simulation;

import java.util.Scanner;

import org.springframework.web.client.ResourceAccessException;

import br.com.fiap.persistence.simulation.helpers.APIHelper;
import br.com.fiap.persistence.simulation.helpers.CategoriaHelper;
import br.com.fiap.persistence.simulation.helpers.ClienteHelper;
import br.com.fiap.persistence.simulation.helpers.PedidosHelper;
import br.com.fiap.persistence.simulation.helpers.ProdutoHelper;
import br.com.fiap.persistence.simulation.helpers.SecaoHelper;

public class GerenciadorEcommerce {

	private String mensagem;

	public void montaMenuInicialECommerce() {

		iniciaMenusDeConsultaDoECommerce();

	}

	private void iniciaMenusDeConsultaDoECommerce() {
		System.out.println("******************************************************************************");
		System.out.println("        		    e-Commerce 	-     Livros FIAP");
		System.out.println("******************************************************************************");
		System.out.println("\n");

		Scanner s = new Scanner(System.in);
		boolean processa = true;
		mensagem = "Escolha uma das opções para iniciar.";
		while (processa) {

			System.out.println("	Escolha uma das opções para iniciar:      ");
			System.out.println("                                              ");
			System.out.println(" 1. Seções da Livraria");
			System.out.println(" 2. Categorias");
			System.out.println(" 3. Livros");
			System.out.println(" 4. Clientes");
			System.out.println(" 5. Consultar Estoque de Livros");
			System.out.println(" 6. Pedidos");
			System.out.println(" 7. Sair");
			System.out.println("\n");
			
			if (mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}
			
			String valorLido = APIHelper.lerTela(s);
			if(valorLido.equals("7")) {
				processa = false;
			} else {
				this.trataLeituraTela(valorLido);	
			}
			
		}
		s.close();

	}

	private void trataLeituraTela(String valorLido) {
		
		try {
			switch (valorLido) {
			case "1":
				SecaoHelper helperSecao = new SecaoHelper();
				helperSecao.montaMenuSecoes(mensagem);
				break;
			case "2":
				CategoriaHelper helperCategoria = new CategoriaHelper();
				helperCategoria.montaMenuCategorias(mensagem);
				break;
			case "3":
				ProdutoHelper helperProduto= new ProdutoHelper ();
				helperProduto.montaMenuProdutos(mensagem);
				break;
			case "4":
				ClienteHelper helperCliente = new ClienteHelper ();
				helperCliente.montaMenuCliente(mensagem);
				break;
			case "5":
				ProdutoHelper helperProduto2= new ProdutoHelper ();
				helperProduto2.imprimeEstoqueDeProdutos(mensagem);
				break;
			case "6":
				PedidosHelper helperPedido= new PedidosHelper ();
				helperPedido.montaMenuPedidos(mensagem);
				break;
			default:
				mensagem = "Digite uma opção válida (1, 2, 3, 4, 5, 6 ou 7)";
			}
		} catch(ResourceAccessException e) {
			System.out.println("***************************************************************************************");
			System.out.println("Não foi possível acessar a API de acesso aos dados. Por favor, verifique a configuração");
			System.out.println("de acesso ao servidor.");
			System.out.println("***************************************************************************************");
			System.out.println("\n");
		}
	}
	
	
	
	
}
