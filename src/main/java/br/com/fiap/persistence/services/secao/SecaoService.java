package br.com.fiap.persistence.services.secao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import br.com.fiap.persistence.entity.Secao;
import br.com.fiap.persistence.repository.SecaoRepository;

/**
 * Classe responsavel por manipular efetivamente o repositorio de Secao
 * 
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Service
public class SecaoService implements ISecaoService {

	@Autowired
	private SecaoRepository secaoRepository;

	/**
	 * Método responsavel por deletar uma secao da tabela Secao pelo seu id
	 */
	@Override
	@Caching(
			evict= {
			@CacheEvict(value= "secaoCache", key= "#id"),
			@CacheEvict(value= "allSecaoCache", allEntries= true) }
	)
	public void deleteSecao(long id) {
		System.out.println("DELETE Secao");
		this.secaoRepository.delete(this.secaoRepository.findById(id).get());
	}

	/**
	 * Método responsavel por atualizar uma secao
	 */
	@Override
	@Caching(
			put= { @CachePut(value= "secaoCache", key= "#secao.id") },
			evict= { @CacheEvict(value= "allSecaoCache", allEntries= true) }
	)
	public Secao updateSecao(Secao secao) {
		System.out.println("UPDATE Secao");
		return this.secaoRepository.save(secao);
	}

	/**
	 * Método responsavel por adicionar uma nova secao na tabela Secao
	 */
	@Caching(
		put = { @CachePut(value= "secaoCache", key= "#secao.id") },
		evict = { @CacheEvict(value= "allSecaoCache", allEntries= true) }
	)
	@Override
	public Secao addSecao(Secao secao) {
		System.out.println("ADD Secao");
		return this.secaoRepository.save(secao);
	}

	/**
	 * Método responsavel por retornar todas as secao da tabela Secao
	 */
	@Override
	@Cacheable(value= "allSecaoCache", unless= "#result.size() == 0")
	public List<Secao> getAllSecao() {
		System.out.println("GetALL Secao");
		List<Secao> listaSecoes = new ArrayList<Secao>();
		this.secaoRepository.findAll().forEach(c -> listaSecoes.add(c));
		return listaSecoes;
	}

	@Override
	@Cacheable(value="secaoCache", key = "#id")
	public Secao getSecaoById(long id) {
		// Busca a Secao pelo ID a partir do repositorio injetado via Dependency Injection
		System.out.println("GetByID Secao");
		try {
			return this.secaoRepository.findById(id).get();			
		} catch(NoSuchElementException e) {
			System.out.println("Não foi possivel encontrar a secao de Id: " + id);
		}
		return null;
	}
	
	public void salvaSecao(Secao secao) {
		System.out.println("SalvaSecao");
		this.secaoRepository.save(secao);
	}

}
