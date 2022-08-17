package com.adobe.aem.guides.wknd.core.dao;

//SERVICE
import com.adobe.aem.guides.wknd.core.service.Fabrica;
//BANCO DE DADOS
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//OSGi
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


@Component(immediate=true, service = ChecaTabela.class)
public class ChecaTabelaImpl implements ChecaTabela{

    @Reference
    private Fabrica f;
    private Statement stm = f.getStatement();

    @Override
    public void checaTudo(){
        checaTabela("cliente","ID int, NOME varchar(999), PRIMARY KEY(ID)");
        checaTabela("produto","ID int, NOME varchar(999), CATEGORIA varchar(999), PRECO double, PRIMARY KEY(ID)");
        checaTabela("notafiscal","NUMERO int, IDCliente int, IDProduto int, PRECO double, PRIMARY KEY(ID)," +
                "FOREIGN KEY (IDCiente) REFERENCES cliente(ID)," +
                "FOREIGN KEY (IDProduto) REFERENCES produto(ID),"
        );
    }

    @Override
    public void checaTabela(String nome, String atributos){
        try {
            String sql = "CREATE TABLE "+ nome +"("+atributos+")";
            //System.out.println(sql);
            this.stm.execute(sql);
        }catch (SQLException e) {
            String msg = e.getMessage();
            if (!msg.equals("Table '" + nome + "' already exists")){
                System.out.println("ERRO AO CHECAR EXISTENCIA DA TABELA " + nome + "! (core/dao/checkTable())");
                System.out.println(msg);
                return;
            }
        }
    }

}
