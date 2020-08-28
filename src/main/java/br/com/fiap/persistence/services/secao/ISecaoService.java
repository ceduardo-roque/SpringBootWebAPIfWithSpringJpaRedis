package br.com.fiap.persistence.services.secao;

import java.util.List;

import br.com.fiap.persistence.entity.Secao;

/**
 * Interface responsável por chamar efetivamente as ações de CRUD de Secao com métodos do Spring Data via Reflection
 * @author Carlos Eduardo Roque da Silva
 *
 */
public interface ISecaoService {

	// Métodos de CRUD da Secao
	void deleteSecao(long id);
	Secao updateSecao(Secao secao);
	Secao addSecao(Secao secao);
	List<Secao> getAllSecao();
	Secao getSecaoById(long id);
	void salvaSecao(Secao secao);
}
