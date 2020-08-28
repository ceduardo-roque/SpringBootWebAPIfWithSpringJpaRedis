package br.com.fiap.persistence.simulation.helpers;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Classe helper para informar os dados da API 
 * 
 * @author Carlos Eduardo Roque da Silva
 *
 */
public abstract class APIHelper {

	private String urlPadrao = "http://localhost:8080/ecommerce_fiap/rest/";

	private String sufixo = "";
	
	public APIHelper(String sufixo) {
		this.sufixo = sufixo;
	}
	
	public String getCaminhoAPIEntidade() {
		return this.urlPadrao + this.sufixo;
	}
	
	public static String lerTela(Scanner s) {
		String valorLido = "";
		try {
			valorLido = s.nextLine();
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
		return valorLido;
	}
	
}
