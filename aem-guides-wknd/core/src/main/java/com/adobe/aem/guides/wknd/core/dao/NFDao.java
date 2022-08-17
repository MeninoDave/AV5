package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
import java.util.Collection;

//"O CODIGO AEM NAO VAI RODAR SE NAO HOUVER IMPLEMENTACOES DE INTERFACE!" -Andre,2022
public interface NFDao {
    void add(NotaFiscal notaFiscal);
    Collection<NotaFiscal> getAll();
    Collection<NotaFiscal> getByID(int id);
}