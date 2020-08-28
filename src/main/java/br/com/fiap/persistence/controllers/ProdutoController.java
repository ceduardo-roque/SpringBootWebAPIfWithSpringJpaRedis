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
import br.com.fiap.persistence.entity.Produto;
import br.com.fiap.persistence.services.categoria.CategoriaService;
import br.com.fiap.persistence.services.produtos.ProdutoService;
/**
 * Classe que uma controller que pode ser invocada via chamadas REST com JSON
 * @author Ayrton Henrique Gomes Silva
 *
 */

@RestController
@RequestMapping("rest/produtos")
public class ProdutoController {
	@Autowired
	private ProdutoService _produtoService;

	@Autowired
	private CategoriaService categoriaService;

	/**
	 * Método que retorna um produto pelo Id
	 * @param id da secao a ser pesquisada
	 * @return ResponseEntity A secao encontrada ou Status de No_Content caso nao encontre
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getProdutoById(@PathVariable("id") Long id) {

		Produto Produto = _produtoService.getProdutoById(id);
		if (Produto != null)
			return new ResponseEntity<Produto>(Produto, HttpStatus.OK);
		else
			return new ResponseEntity<Produto>(Produto, HttpStatus.NO_CONTENT);
	}

	/**
	 * Método que retorna todas as Produtos da tabela de Produto
	 * @return ResponseEntity OK com a listagem dos produtos
	 */
	@GetMapping()
	public ResponseEntity<List<Produto>> getAllSProdutos() {
		return new ResponseEntity<List<Produto>>(_produtoService.getAllProduto(), HttpStatus.OK);
	}

	/**
	 * Método que cadastra uma nova Produto na tabela Produto
	 * 
	 * @param produto A Produto a ser inserida
	 * @param builder Builder injetado automaticamente pelo Spring pra manipulação
	 *                de caminho relativo
	 * @return Um objeto ResponseEntity com status de CREATED
	 */
	@PostMapping()
	public ResponseEntity<Void> addProduto(@RequestBody Produto produto, UriComponentsBuilder builder) {
		Categoria categoria = produto.getCategoria();
		HttpHeaders headers = new HttpHeaders();

		if (categoria == null) // Produto sem categoria definida
			return new ResponseEntity<Void>(headers, HttpStatus.BAD_REQUEST);
		else {
			// Recupera Categoria Managed
			Categoria categoriaManaged = categoriaService.getCategoriaByDescricao(categoria.getDescricao());
			produto.setCategoria(categoriaManaged);
			Produto produtoCriado = _produtoService.salvarProduto(produto);

			// Manipula os Headers da Resposta e redireciona pra pesquisa da Produto pelo			// novo Id criado
			headers.setLocation(builder.path("/Produto/{id}").buildAndExpand(produtoCriado.getId()).toUri());
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		}
	}

	/**
	 * Método que atualiza uma Produto
	 * 
	 * @param Produto A Produto a ser atualizada
	 * @return Um objeto ResponseEntity com o status de OK ou com o status de
	 *         NOT_FOUND quando não encontra a Produto a ser atualizada
	 */
	@PutMapping()
	public ResponseEntity<Produto> updateProduto(@RequestBody Produto Produto) {
		System.out.println("UPDATE: " + Produto);
		Produto ProdutoParaAtualizar = _produtoService.getProdutoById(Produto.getId());
		if (ProdutoParaAtualizar != null) {
			_produtoService.salvarProduto(Produto);
			return new ResponseEntity<Produto>(Produto, HttpStatus.OK);
		} else
			return new ResponseEntity<Produto>(Produto, HttpStatus.NOT_FOUND);
	}

	/**
	 * Método que deleta uma Produto da tabela de Produtos
	 * 
	 * @param id O id da Produto a ser deletada da tabela
	 * @return Um objeto ResponseEntity com o status de OK para o pedido deletado
	 *         NOT_FOUND quando não deletou o produto.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
		Produto ProdutoParaAtualizar = _produtoService.getProdutoById(id);
		if (ProdutoParaAtualizar != null) {
			_produtoService.deleteProduto(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

}