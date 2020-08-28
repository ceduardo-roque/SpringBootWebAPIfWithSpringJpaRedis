package br.com.fiap.persistence.services.categoria;

import java.util.List;

import br.com.fiap.persistence.entity.Categoria;

/**
 * Interface responsável por chamar efetivamente as ações de CRUD de categoria com métodos do Spring Data via Reflection
 * @author Carlos Eduardo Roque da Silva
 *
 */
public interface ICategoriaService {

	// Métodos de CRUD da Categoria
	void deleteCategoria(long id);
	Categoria updateCategoria(Categoria categoria);
	Categoria addCategoria(Categoria categoria);
	List<Categoria> getAllCategoria();
	Categoria getCategoriaById(long id);
	Categoria getCategoriaByDescricao(String descricao);
}
