package com.adobe.aem.guides.wknd.core.service;

//SLING
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

//"O CODIGO AEM NAO VAI RODAR SE NAO HOUVER IMPLEMENTACOES DE INTERFACE!" -Andre,2022
public interface ServicoServletProduto {

    //REQUISICOES HTTP
    void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response);

}