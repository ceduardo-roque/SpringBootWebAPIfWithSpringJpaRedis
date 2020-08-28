package br.com.fiap.persistence.services.produtos;

import java.util.List;

import br.com.fiap.persistence.entity.Produto;

public interface IProdutoService {
    void deleteProduto(long id);
	List<Produto> getAllProduto();
	Produto getProdutoById(long id);
	Produto salvarProduto(Produto Produto);
}