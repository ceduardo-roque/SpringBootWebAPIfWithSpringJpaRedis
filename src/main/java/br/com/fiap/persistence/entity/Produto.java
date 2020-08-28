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
/**
 * Classe que representa a entidade Produto no banco de dados
 * @author Ayrton Henrique Gomes Silva 
 *
 */
@Entity
@Table(name = "Produto")
public class Produto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_produto")
    private long id;

    private String descricao;
    private int quantidadeEstoque;
    private double preco;

    @ManyToOne()
    @JoinColumn(name="id_categoria",nullable =  true, unique = false)
    private Categoria categoria;

    public Produto() {
        super();
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " - " + this.descricao + " - Preço: " + this.getPreco() + " - QTDE Estoque: " + this.getQuantidadeEstoque();
    }

    public Produto(long id, String descricao, int quantidadeEstoque, double preco, Categoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.quantidadeEstoque = quantidadeEstoque;
        this.setPreco(preco);
        this.setCategoria(categoria);
    }

}