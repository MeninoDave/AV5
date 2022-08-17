package com.adobe.aem.guides.wknd.core.models;

public class NotaFiscal{
    //Declaracao
    private int numero;
    private int idProduto;
    private int idCliente;
    private double valor;

    //Construtor
    public NotaFiscal(){}

    public NotaFiscal(int numero,int idCliente,int idProduto,double valor){
        this.numero=numero;
        this.idCliente=idCliente;
        this.idProduto=idProduto;
        this.valor=valor;
    }
    public int getNumero(){
        return this.numero;
    }
    public int getIdProduto(){
        return this.idProduto;
    }
    public int getIdCliente(){
        return this.idCliente;
    }
    public double getValor(){
        return this.valor;
    }

}