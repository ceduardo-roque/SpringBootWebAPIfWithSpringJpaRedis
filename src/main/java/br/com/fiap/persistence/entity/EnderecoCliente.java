package br.com.fiap.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.fiap.persistence.enums.TipoEndereco;
/**
 * Classe que representa a entidade EnderecoCliente no banco de dados
 * @author Sara Pires
 *
 */
@Entity
@Table(name = "EnderecoCliente")
@JsonIgnoreProperties(value= {"cliente"})
public class EnderecoCliente implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_endereco")
	private long id;
	@Column(name = "nm_logradouro")
	private String logradouro;
	@Column(name = "nr_numero")
	private Integer numero;
	@Column(name = "ds_complemento")
	private String complemento;
	@Column(name = "nr_cep")
	private String cep;
	@Column(name = "ds_cidade")
	private String cidade;
	@Column(name = "ds_estado")
	private String estado;
	@Column(name = "tipo_endereco")
	private TipoEndereco tipoEndereco;
	
	@ManyToOne()
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	
	public EnderecoCliente() {
		super();
	}

	

	public EnderecoCliente(String logradouro, Integer numero, String complemento, String cep, String cidade, String estado,
			 TipoEndereco tipoEndereco, Cliente cliente) {
		super();
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		this.tipoEndereco = tipoEndereco;
		this.cliente = cliente;
	}



	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getLogradouro() {
		return logradouro;
	}


	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}


	public Integer getNumero() {
		return numero;
	}


	public void setNumero(Integer numero) {
		this.numero = numero;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getCidade() {
		return cidade;
	}


	public void setCidade(String cidade) {
		this.cidade = cidade;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	@Override
	public String toString() {
		return  "\t" + (this.tipoEndereco  == TipoEndereco.RESIDENCIAL ? "Residencial" : "Entrega") 
				+ "\n\tRua: " + this.logradouro + ", " + this.numero 
				+ " - " + this.complemento 
				+ " - " + this.cep 
				+ "\n\t" + this.cidade 
				+ " - " + this.estado 
				+ "\n";
	}
}
