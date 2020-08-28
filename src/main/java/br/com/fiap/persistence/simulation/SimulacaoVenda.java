package br.com.fiap.persistence.simulation;

/**
 * Classe de para sumular as vendas de produtos no e-commerce
 * @author Carlos Eduardo Roque da Silva
 */
public class SimulacaoVenda {
	
	public static void main(String args[]) {
//		SpringApplication.run(RunApplication.class, args);
//		InfraestruturaInicialLoad.carregaInfraEstruturaBasica();
		GerenciadorEcommerce gerenciador = new GerenciadorEcommerce();
		gerenciador.montaMenuInicialECommerce();
	}

}
