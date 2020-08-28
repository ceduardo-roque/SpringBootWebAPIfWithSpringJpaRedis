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
/**
 * Classe que representa a entidade Item_Pedido no banco de dados
 * @author Willian Yoshiaki Kazahaya
 *
 */
@Entity
@Table(name= "item_pedido")
@JsonIgnoreProperties(value= {"pedido"})
public class ItemPedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_item")
    private long id;
    
    @ManyToOne()
    @JoinColumn(name="id_produto")
	private Produto produto;
	
	@ManyToOne()
	@JoinColumn(name="id_pedido")
	private Pedido pedido;

	@Column(name= "quantidade_item")
	private int quantidade;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public ItemPedido() {
		super();
	}
	
	public ItemPedido(Produto produto, int quantidade) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
	}
	
	@Override
	public String toString() {
		return "Código: " + this.id + " produto: " + this.produto.toString() + " quantidade do item: " + this.quantidade;
	}
	
}
