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

/**
 * Classe que representa a entidade Cliente no banco de dados
 * @author Sara Pires
 *
 */

@Entity
@Table(name = "Cliente")
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cliente")
	private long id;
	
	@Column(name = "nm_cliente")
	private String nmCliente;
	
	@Column(name = "nr_telefone")
	private Integer nrTelefone;

	@Column(name = "email")
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<EnderecoCliente> enderecos = new HashSet<EnderecoCliente>(); 
	
	public Cliente() {
		super();
	}


	public Cliente(long id, String nmCliente) {
		super();
		this.id = id;
		this.nmCliente = nmCliente;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNmCliente() {
		return nmCliente;
	}


	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}
	
	public Integer getNrTelefone() {
		return nrTelefone;
	}

	public void setNrTelefone(Integer nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	
	public Set<EnderecoCliente> getEnderecos() {
		return enderecos;
	}


	public void setEnderecos(Set<EnderecoCliente> enderecos) {
		this.enderecos = enderecos;
	}
	
	public void addEndereco(EnderecoCliente enderecos) {
		this.enderecos.add(enderecos);
	}


	@Override
	public String toString() {
		String cliente = "ID:       " + this.id + " \nNome:      " + this.nmCliente + " \nTelefone:  " + 
								  this.nrTelefone + " \nEmail:     " + this.email + "\n";
		for (EnderecoCliente enderecoCliente : enderecos) {
			cliente += "\tEndereço: " + enderecoCliente.toString();
			
		}
		return cliente;
	}

	
}
