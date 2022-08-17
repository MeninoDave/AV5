package com.adobe.aem.guides.wknd.core.service;
import java.sql.Connection;
import java.sql.Statement;

//"O CODIGO AEM NAO VAI RODAR SE NAO HOUVER IMPLEMENTACOES DE INTERFACE!" -Andre,2022
public interface Fabrica {
    Connection getConnection();
    Statement getStatement();
}