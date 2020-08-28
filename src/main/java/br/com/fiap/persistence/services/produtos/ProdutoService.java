package br.com.fiap.persistence.services.produtos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import org.springframework.stereotype.Service;

import br.com.fiap.persistence.entity.Produto;
import br.com.fiap.persistence.repository.ProdutoRepository;

@Service
public class ProdutoService implements IProdutoService {

    @Autowired
    private ProdutoRepository _produtoRepository;

    @Override
    public void deleteProduto(long id) {
        _produtoRepository.deleteById(id);

    }

    @Caching(
		put = { @CachePut(value= "produtoCache", key= "#Produto.id") },
		evict = { @CacheEvict(value= "allProducaoCache", allEntries= true) }
	)
    @Override
    public Produto salvarProduto(Produto Produto) {

        return _produtoRepository.save(Produto);
    }

    @Override
    // @Cacheable(value= "allProdutos", unless= "#result.size() == 0")
    public List<Produto> getAllProduto() {
        
        return _produtoRepository.findAll();
    }

    @Override
    @Cacheable(value="produtoCache", key = "#id")
    public Produto getProdutoById(long id) {
        Optional<Produto> produto = _produtoRepository.findById(id);
        return  produto.isPresent() ? produto.get() : null;
    }

   
}