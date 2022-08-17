package com.adobe.aem.guides.wknd.core.service;

//SQL
import java.sql.*;
//OSGi
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
//DataSourcePool
import com.day.commons.datasource.poolservice.DataSourcePool;
//Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//javax
import javax.sql.DataSource;
import java.sql.Connection;

//Essa clase ira fazer a conexao com o Banco de Dados MySQL
@Component(immediate=true,service = Fabrica.class)
public class FabricaImpl implements Fabrica{
    private final Logger logger = LoggerFactory.getLogger(FabricaImpl.class);

    @Reference
    private DataSourcePool DSP;
    Connection connection = null;

    @Override
    public Connection getConnection(){
       try {
            DataSource dataSource = (DataSource) DSP.getDataSource("av5");
            this.connection = dataSource.getConnection();
            logger.debug("Conexão obtida com sucesso!");
       }
       catch (Exception e) {
            logger.debug("ERRO! Não foi possivel se conectar com o Banco de Dados" + e.getMessage());
       }
        return connection;
    }


    @Override
    public Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch(SQLException e) {
            System.out.println("ERRO AO OBTER STATEMENT (service/BancoDeDadosImpl/getStatement())");
            System.out.println(e.getMessage());
            return null;
        }

    }
}