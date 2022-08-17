package com.adobe.aem.guides.wknd.core.dao;

//MODELS
import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
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
//Outros
import java.util.ArrayList;
import java.util.Collection;

@Component(immediate=true, service = NFDao.class)
public class NFDaoImpl implements NFDao{

    @Reference
    private Fabrica f;

    @Override
    public void add(NotaFiscal notaFiscal){
        try(Connection cn = this.f.getConnection()){
            String sql = "INSERT INTO notafiscal VALUES (?,?,?,?)";
            PreparedStatement ps = cn.prepareStatement(sql);
            try{
                ps.setInt(1,notaFiscal.getNumero());
                ps.setInt(2,notaFiscal.getIdCliente());
                ps.setInt(3,notaFiscal.getIdProduto());
                ps.setDouble(4,notaFiscal.getValor());

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel salvar nota no BD (core/dao/NFDaoImpl/add())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/NFDaoImpl/add())\n"+e.getMessage());
        }
    }

    @Override
    public Collection<NotaFiscal> getAll(){
        try(Connection cn = this.f.getConnection()){
            ArrayList<NotaFiscal> result = new ArrayList<NotaFiscal>();
            String sql = "SELECT * FROM notafiscal";
            PreparedStatement ps = cn.prepareStatement(sql);
            try{
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()){
                    NotaFiscal nf = new NotaFiscal(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getDouble(4));
                    result.add(nf);
                }
                return result;
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel obter valores do BD (core/dao/NFDaoImpl/getAll())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/NFDaoImpl/getAll())\n"+e.getMessage());
        }
    }

    @Override
    public Collection <NotaFiscal> getByID(int idCliente){
        try(Connection cn = this.f.getConnection()){
            ArrayList<NotaFiscal> result = new ArrayList<NotaFiscal>();
            String sql = "SELECT * FROM notafiscal WHERE IDCliente="+idCliente;
            PreparedStatement ps = cn.prepareStatement(sql);
            try{
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()){
                    NotaFiscal nf = new NotaFiscal(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getDouble(4));
                    result.add(nf);
                }
                return result;
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel obter valores do BD (core/dao/NFDaoImpl/getByID())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/NFDaoImpl/getByID())\n"+e.getMessage());
        }
    }
}