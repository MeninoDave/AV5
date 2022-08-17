package com.adobe.aem.guides.wknd.core.dao;

//MODEL
import com.adobe.aem.guides.wknd.core.models.Cliente;
//SERVICE
import com.adobe.aem.guides.wknd.core.service.Fabrica;
//BANCO DE DADOS
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//OSGi
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
//UTIL
import java.util.ArrayList;
import java.util.Collection;

@Component(immediate=true, service = ClienteDao.class)
public class ClienteDaoImpl implements ClienteDao{
    @Reference
    private Fabrica fabrica;

    @Override
    public void add(Cliente cliente){
        try(Connection cn = this.fabrica.getConnection()){
            String sql = "INSERT INTO cliente VALUES (?,?)";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setInt(1,cliente.getID());
                ps.setString(2,cliente.getNome());

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel salvar nota no BD (core/dao/ClienteDaoImpl/add())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ClienteDaoImpl/add())\n"+e.getMessage());
        }
    }

    @Override
    public void update(Cliente cliente, String nome){
        try(Connection cn = this.fabrica.getConnection()){
            String sql = "UPDATE cliente SET NOME = ? WHERE ID = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            try{
                ps.setString(1,nome);
                ps.setInt(2,cliente.getID());

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel realizar alteracao no BD (core/dao/ClienteDaoImpl/update())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ClienteDaoImpl/update())\n"+e.getMessage());
        }
    }

    @Override
    public void delete(int id){
        try(Connection cn = this.fabrica.getConnection()){
            String sql = "DELETE FROM cliente WHERE ID = ?";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setInt(1,id);
                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel deletar valor no BD (core/dao/ClienteDaoImpl/delete())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ClienteDaoImpl/delete())\n"+e.getMessage());
        }
    }

    @Override
    public Cliente pesquisa(String nome){
        try(Connection cn = this.fabrica.getConnection()){
            String sql = "SELECT * FROM cliente WHERE NOME= ?";
            Cliente result = null;
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setString(1,nome);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()){
                    result= new Cliente(rs.getInt(1),rs.getString(2));
                }
                return result;
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel obter valores do BD (core/dao/ClienteDaoImpl/search())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ClienteDaoImpl/search())\n"+e.getMessage());
        }
    }

    @Override
    public Collection<Cliente> getAll(){
        try(Connection cn = this.fabrica.getConnection()){
            String sql = "SELECT * FROM cliente";
            PreparedStatement ps = cn.prepareStatement(sql);
            Collection<Cliente> result = new ArrayList<Cliente>();
            try{
                ps.execute();
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    result.add(new Cliente(rs.getInt(1),rs.getString(2)));
                }
                return result;
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel obter valores do BD (core/dao/ClienteDaoImpl/getAll())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ClienteDaoImpl/getAll())\n"+e.getMessage());
        }
    }
}