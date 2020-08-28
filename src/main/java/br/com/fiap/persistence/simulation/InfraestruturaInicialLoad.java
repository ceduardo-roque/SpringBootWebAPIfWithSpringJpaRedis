package br.com.fiap.persistence.simulation;

import br.com.fiap.persistence.simulation.infrastructure.LoadDadosLivraria;

/**
 * Classe de testes da entidade Categoria
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class InfraestruturaInicialLoad {

	public static String urlPadrao = "http://localhost:8080/ecommerce_fiap/rest/";

	public static void carregaInfraEstruturaBasica() {
		new LoadDadosLivraria().loadDadosDaLivraria();
	}


}
