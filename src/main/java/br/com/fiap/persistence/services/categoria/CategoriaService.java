package br.com.fiap.persistence.services.categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import br.com.fiap.persistence.entity.Categoria;
import br.com.fiap.persistence.repository.CategoriaRepository;

/**
 * Classe responsavel por manipular efetivamente o repositorio de Categoria
 * 
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Service
public class CategoriaService implements ICategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	/**
	 * Método responsavel por deletar uma categoria da tabela Categoria pelo seu id
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = "categoriaCache", key = "#id"),
			@CacheEvict(value = "allCategoriaCache", allEntries = true) })
	public void deleteCategoria(long id) {
		System.out.println("DELETE Categoria");
		this.categoriaRepository.delete(this.categoriaRepository.findById(id).get());
	}

	/**
	 * Método responsavel por atualizar uma cateogoria
	 */
	@Override
	@Caching(put = { @CachePut(value = "categoriaCache", key = "#categoria.id") }, evict = {
			@CacheEvict(value = "allCategoriaCache", allEntries = true) })
	public Categoria updateCategoria(Categoria categoria) {
		System.out.println("UPDATE Categoria");
		return this.categoriaRepository.save(categoria);
	}

	/**
	 * Método responsavel por adicionar uma nova categoria na tabela Categoria
	 */
	@Caching(put = { @CachePut(value = "categoriaCache", key = "#categoria.id") }, evict = {
			@CacheEvict(value = "allCategoriaCache", allEntries = true) })
	@Override
	public Categoria addCategoria(Categoria categoria) {
		System.out.println("ADD Categoria");
		return this.categoriaRepository.save(categoria);
	}

	/**
	 * Método responsavel por retornar todas as categorias da tabela Categoria
	 */
	@Override
	@Cacheable(value = "allCategoriaCache", unless = "#result.size() == 0")
	public List<Categoria> getAllCategoria() {
		System.out.println("GetALL Categoria");
		List<Categoria> categorias = new ArrayList<Categoria>();
		try {
			categorias = this.categoriaRepository.findAll();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// this.categoriaRepository.findAll().forEach(c -> listaCategorias.add(c));
		return categorias;
	}

	@Override
	@Cacheable(value = "categoriaCache", key = "#id")
	public Categoria getCategoriaById(long id) {
		// Busca a Categoria pelo ID a partir do repositorio injetado via Dependency
		// Injection
		System.out.println("GetByID Categoria");
		try {
			return this.categoriaRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			System.out.println("Não foi possivel encontrar a categoria de Id: " + id);
		}
		return null;
	}

	public Categoria getCategoriaByDescricao(String descricao) {
		System.out.println("GetByDs_Categoria");
		try {
			return this.categoriaRepository.findByDescricao(descricao);
		} catch (NoSuchElementException e) {
			System.out.println("Não foi possivel encontrar a categoria de descricao: " + descricao);
		}
		return null;
		
	}

}
