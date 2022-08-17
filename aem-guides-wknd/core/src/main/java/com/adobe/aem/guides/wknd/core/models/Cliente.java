package com.adobe.aem.guides.wknd.core.models;

public class Cliente{
    //Declaracao
    private int id;
    private String nome;

    //Construtor
    public Cliente(){}
    public Cliente(int id,String nome){
        this.id=id;
        this.nome=nome;
    }
    public int getID(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
}