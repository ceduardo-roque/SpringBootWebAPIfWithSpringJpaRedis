package br.com.fiap.persistence.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.persistence.entity.Categoria;
import br.com.fiap.persistence.entity.Secao;
import br.com.fiap.persistence.services.secao.ISecaoService;

/**
 * Classe que uma controller que pode ser invocada via chamadas REST com JSON
 * @author Carlos Eduardo Roque da Silva
 *
 */
@RestController
@RequestMapping("rest")
public class SecaoController {

	@Autowired
	private ISecaoService secaoService;
	
	/**
	 * Método que retorna uma secao pelo Id
	 * @param id da secao a ser pesquisada
	 * @return A secao encontrada ou Status de No_Content caso nao encontre
	 */
	@GetMapping("secao/{id}")
	public ResponseEntity<Secao> getSecaoById(@PathVariable("id") Long id) {
		System.out.println("GET: " + id);
		// tratar secao nao encontrada
		Secao secao = this.secaoService.getSecaoById(id);
		if(secao != null)
			return new ResponseEntity<Secao>(secao, HttpStatus.OK);
		else
			return new ResponseEntity<Secao>(secao, HttpStatus.NO_CONTENT);
		
	}
	
	/**
	 * Método que retorna todas as secaos da tabela de secao
	 * @return
	 */
	@GetMapping("secao")
	public ResponseEntity<List<Secao>> getAllSecoes() {
		System.out.println("GETALL");
		List<Secao> listaDeSecoes = this.secaoService.getAllSecao();
		return new ResponseEntity<List<Secao>>(listaDeSecoes, HttpStatus.OK);
		
	}
	
	/**
	 * Método que cadastra uma nova secao na tabela secao
	 * @param secao A secao a ser inserida
	 * @param builder Builder injetado automaticamente pelo Spring pra manipulação de caminho relativo
	 * @return Um objeto ResponseEntity com status de CREATED
	 */
	@PostMapping("secao")
	public ResponseEntity<Void> addSecao(@RequestBody Secao secao, UriComponentsBuilder builder) {
		System.out.println("ADD: " + secao);
		Secao secaoCriada = this.secaoService.addSecao(secao);
		
		// Manipula os Headers da Resposta e redireciona pra pesquisa da secao pelo novo Id criado
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/secao/{id}").buildAndExpand(secaoCriada.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	
	/**
	 * Método que cadastra uma nova secao na tabela secao
	 * @param secao A secao a ser inserida
	 * @param builder Builder injetado automaticamente pelo Spring pra manipulação de caminho relativo
	 * @return Um objeto ResponseEntity com status de CREATED
	 */
	@GetMapping("secao/categorias/{id}")
	public ResponseEntity<List<Categoria>> getCategoriasNaSecao(@PathVariable("id") Long id) {
		System.out.println("GET CATEGORIAS NA SECAO: " + id);
		
		Secao secaoManaged = this.secaoService.getSecaoById(id);
		System.out.println("Categorias encontradas: " + secaoManaged.getCategorias().size());;
		
		Set<Categoria> categorias = secaoManaged.getCategorias();
		List<Categoria> lista = categorias.stream().collect(Collectors.toCollection(ArrayList::new));
		return new ResponseEntity<List<Categoria>>(lista, HttpStatus.OK);
	}
	
	
	/**
	 * Método que atualiza uma secao
	 * @param secao A secao a ser atualizada
	 * @return Um objeto ResponseEntity com o status de OK ou com o status de NOT_FOUND quando não encontra a secao a ser atualizada
	 */
	@PutMapping("secao")
	public ResponseEntity<Secao> updateSecao(@RequestBody Secao secao) {
		System.out.println("UPDATE: " + secao);
		Secao secaoParaAtualizar = this.secaoService.getSecaoById(secao.getId());
		if(secaoParaAtualizar!=null) {
			this.secaoService.updateSecao(secao);
			return new ResponseEntity<Secao>(secao, HttpStatus.OK);
		} else return new ResponseEntity<Secao>(secao, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Método que deleta uma secao da tabela de secaos
	 * @param id O id da secao a ser deletada da tabela
	 * @return 
	 */
	@DeleteMapping("secao/{id}")
	public ResponseEntity<Void> deleteSecao(@PathVariable("id") Long id) {
		System.out.println("DELETE: " + id);
		Secao secaoParaAtualizar = this.secaoService.getSecaoById(id);
		if(secaoParaAtualizar!=null) {
			this.secaoService.deleteSecao(id);
			return new ResponseEntity<Void>(HttpStatus.OK);			
		} else 
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}
	
}
