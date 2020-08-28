package br.com.fiap.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe que representa a entidade Categoria no banco de dados
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_categoria")
	private long id;
	
	@Column(name = "ds_categoria")
	private String descricao;

	@ManyToOne()
	@JoinColumn(name="id_secao")
	private Secao secao;
	
	public Categoria() {
		super();
	}
	
	
	
	public Categoria(String descricao) {
		super();
		this.descricao = descricao;
	}

	public Categoria(long id, String descricao) {
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
	
	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}

	@Override
	public String toString() {
		return this.id + " - " + this.descricao;
	}
}
