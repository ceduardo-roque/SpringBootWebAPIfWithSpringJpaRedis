package br.com.fiap.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.fiap.persistence.enums.StatusPedido;
/**
 * Classe que representa a entidade Pedido no banco de dados
 * @author Willian Yoshiaki Kazahaya
 *
 */
@Entity
@Table(name= "pedido")
public class Pedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id_pedido", nullable=false)
	@GeneratedValue( strategy = GenerationType.AUTO)
	private long id;
	
	private LocalDateTime dataPedido;
	private long valorTotal;
	private StatusPedido status;

	@ManyToOne()
	@JoinColumn(name="id_cliente")
	private Cliente cliente;
	
	@OneToMany(mappedBy="pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ItemPedido> itens = new LinkedHashSet<ItemPedido>();
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}

	public long getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(long valorTotal) {
		this.valorTotal = valorTotal;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Set<ItemPedido> getItensPedido() {
		return itens;
	}
	
	public void setItensPedido(Set<ItemPedido> itensPedido) {
		this.itens = itensPedido;
	}

	public void addItemPedido(ItemPedido item) {
		this.itens.add(item);
	}
	
	public Pedido() {
		super();
	}
	
	public Pedido(Cliente cliente, LocalDateTime dataPedido) {
		super();
		setItensPedido(new LinkedHashSet<ItemPedido>());
		setCliente(cliente);
		setStatus(StatusPedido.ABERTO);
		setDataPedido(dataPedido);
		setValorTotal(0);
	}
	
	@Override
	public String toString() {
		return    "Pedido Número: " + this.id + "\n" 
				+ "Cliente: "  + this.cliente.toString() + "\n" 
				+ "Status: " + this.status + "\n" 
				+ "Valor: " + this.valorTotal + "\n" 
				+ "Itens: \n" + imprimeItens(this.itens) + "\n";
	}

	private String imprimeItens(Set<ItemPedido> itens2) {
		String retorno = "";
		for (ItemPedido itemPedido : itens2) {
			retorno += "Item: " + itemPedido.toString() + "\n";
		}
		return retorno;
	}

	public void clearItens() {
		// TODO Auto-generated method stub
		this.itens = new LinkedHashSet<ItemPedido>();
	}

	
}
