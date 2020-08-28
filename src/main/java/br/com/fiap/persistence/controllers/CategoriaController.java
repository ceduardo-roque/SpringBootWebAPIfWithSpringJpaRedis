package br.com.fiap.persistence.controllers;


import java.util.List;

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
import br.com.fiap.persistence.services.categoria.ICategoriaService;
import br.com.fiap.persistence.services.secao.ISecaoService;

/**
 * Classe que uma controller que pode ser invocada via chamadas REST com JSON
 * @author Carlos Eduardo Roque da Silva
 *
 */
@RestController
@RequestMapping("rest")
public class CategoriaController {

	@Autowired
	private ICategoriaService categoriaService;
	
	@Autowired
	private ISecaoService secaoService;
	
	
	/**
	 * Método que retorna uma categoria pelo Id
	 * @param id da categoria a ser pesquisada
	 * @return A categoria encontrada ou Status de No_Content caso nao encontre
	 */
	@GetMapping("categoria/{id}")
	public ResponseEntity<Categoria> getCategoriaById(@PathVariable("id") Long id) {
		System.out.println("GET: " + id);
		// tratar categoria nao encontrada
		Categoria categoria = this.categoriaService.getCategoriaById(id);
		if(categoria != null)
			return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
		else
			return new ResponseEntity<Categoria>(categoria, HttpStatus.NO_CONTENT);
		
	}
	
	/**
	 * Método que retorna todas as categorias da tabela de categoria
	 * @return Um objeto ResponseEntity com status OK
	 */
	@GetMapping("categoria")
	public ResponseEntity<List<Categoria>> getAllCategorias() {
		System.out.println("GETALL");
		List<Categoria> listaDeCategorias = this.categoriaService.getAllCategoria();
		return new ResponseEntity<List<Categoria>>(listaDeCategorias, HttpStatus.OK);
		
	}
	
	/**
	 * Método que cadastra uma nova categoria na tabela categoria
	 * @param categoria A categoria a ser inserida
	 * @param builder Builder injetado automaticamente pelo Spring pra manipulação de caminho relativo
	 * @return Um objeto ResponseEntity com status de CREATED
	 */
	@PostMapping("categoria")
	public ResponseEntity<Void> addCategoria(@RequestBody Categoria categoria, UriComponentsBuilder builder) {
		System.out.println("ADD: " + categoria);
		Categoria categoriaCriada = this.categoriaService.addCategoria(categoria);
		
		// Manipula os Headers da Resposta e redireciona pra pesquisa da categoria pelo novo Id criado
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/categorias/{id}").buildAndExpand(categoriaCriada.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * Método que atualiza uma categoria
	 * @param categoria A categoria a ser atualizada
	 * @return Um objeto ResponseEntity com o status de OK ou com o status de NOT_FOUND quando não encontra a categoria a ser atualizada
	 */
	@PutMapping("categoria")
	public ResponseEntity<Categoria> updateCategoria(@RequestBody Categoria categoria) {
		System.out.println("UPDATE: " + categoria);
		Categoria categoriaParaAtualizar = this.categoriaService.getCategoriaById(categoria.getId());
		if(categoriaParaAtualizar!=null) {
			this.categoriaService.updateCategoria(categoria);
			return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
		} else return new ResponseEntity<Categoria>(categoria, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Método que deleta uma categoria da tabela de categorias
	 * @param id O id da categoria a ser deletada da tabela
	 * @return Um objeto ResponseEntity com status OK para a categoria deletada e NOT FOUND para a categoria não encontrada. 
	 */
	@DeleteMapping("categoria/{id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
		System.out.println("DELETE: " + id);
		Categoria categoriaParaAtualizar = this.categoriaService.getCategoriaById(id);
		if(categoriaParaAtualizar!=null) {
			this.categoriaService.deleteCategoria(id);
			return new ResponseEntity<Void>(HttpStatus.OK);			
		} else 
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Método que cadastra uma nova categoria vinculada a uma seção 
	 * @param secao A secao na qual a categoria será inserida com as categorias a serem inseridas populadas
	 * @return Um objeto ResponseEntity com status de CREATED
	 */
	@PostMapping("/categoria/secao/{id}")
	public ResponseEntity<Void> addCategoriaNaSecao(@RequestBody Categoria categoria, @PathVariable("id") Long id) {
		System.out.println("ADD CATEGORIA NA SECAO: " + id);
		Secao secaoManaged = this.secaoService.getSecaoById(id);
		if(secaoManaged!=null) {
			secaoManaged.getCategorias().add(categoria);
			categoria.setSecao(secaoManaged);
			this.secaoService.salvaSecao(secaoManaged);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}
	
}
