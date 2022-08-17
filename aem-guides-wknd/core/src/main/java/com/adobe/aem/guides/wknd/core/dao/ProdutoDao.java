package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Produto;
import java.util.ArrayList;
import java.util.Collection;

//"O CODIGO AEM NAO VAI RODAR SE NAO HOUVER IMPLEMENTACOES DE INTERFACE!" -Andre,2022
public interface ProdutoDao{
    void add(Produto produto);
    Collection<Produto> getAll(String metodo);
    Collection<Produto> pesquisa(String nome,String metodo);
    void updateName(int id,String nome);
    void updateCategoria(int id,String categoria);
    void updatePreco(int id, double preco);
    void updateAll(int id,String nome,String categoria,double preco);
    void delete(int id);
}