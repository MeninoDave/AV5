package com.adobe.aem.guides.wknd.core.models;

public class Produto{
    //Declaracao
    private int id;
    private String nome;
    private String categoria;
    private double preco;

    //Construtor
    public Produto(){}
    public Produto(int id,String nome, String categoria, double preco){
        this.id=id;
        this.nome=nome;
        this.categoria=categoria;
        this.preco=preco;
    }

    public int getID(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome=nome;
    }
    public String getCategoria(){
        return this.categoria;
    }
    public void setCategoria(String categoria){
        this.categoria=categoria;
    }
    public double getPreco(){
        return this.preco;
    }
    public void setPreco(double preco){
        this.preco=preco;
    }
}