package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import java.util.Collection;

//"O CODIGO AEM NAO VAI RODAR SE NAO HOUVER IMPLEMENTACOES DE INTERFACE!" -Andre,2022
public interface ClienteDao{
    void add(Cliente cliente);
    void update(Cliente cliente, String nome);
    void delete(int id);
    Cliente pesquisa(String nome);
    Collection<Cliente> getAll();
}