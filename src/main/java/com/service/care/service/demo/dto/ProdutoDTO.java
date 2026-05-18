package com.service.care.service.demo.dto;
public class ProdutoDTO {
    private String nome;
    private double preco;
    private int quantidade;
    private String codigo;
    private String descricao;
    private Double valor;

    // Construtores, Getters e Setters
    public ProdutoDTO() {}

    public ProdutoDTO(String nome, double preco, int quantidade, String codigo, String descricao, Double valor) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.codigo = codigo;
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public String getCodigo() { return codigo;}
    public void setCodigo(String codigo) { this.codigo = codigo;}
    public String getDescricao() { return descricao;}
    public void setDescricao(String descricao) { this.descricao = descricao;}
    public Double getValor() { return valor;}
    public void setValor(Double valor) { this.valor = valor;}
}