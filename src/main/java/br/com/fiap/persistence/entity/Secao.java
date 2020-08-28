package br.com.fiap.persistence.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe que representa a entidade Secao no banco de dados
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Entity
@Table(name = "secao")
@JsonIgnoreProperties(value= {"categorias"})
public class Secao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_secao")
	private long id;
	
	@Column(name="ds_secao", length = 40)
	private String descricao;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "secao")
	private Set<Categoria> categorias = new HashSet<Categoria>();
	
	public Secao() {
		super();
	}

	public Secao(long id, String descricao) {
		super();
		this.id = id;
		this.descricao = descricao;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	@Override
	public String toString() {
		return this.id + " - " + this.descricao;
	}

	public void imprimeCategorias() {
		for (Categoria categoria : categorias) {
			System.out.println(categoria.toString());
		}
	}
	
	
}
