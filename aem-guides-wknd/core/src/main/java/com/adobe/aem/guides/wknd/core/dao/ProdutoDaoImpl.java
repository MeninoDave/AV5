package com.adobe.aem.guides.wknd.core.dao;

//MODEL
import com.adobe.aem.guides.wknd.core.models.Produto;
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
//OUTROS
import java.util.ArrayList;
import java.util.Collection;

@Component(immediate=true, service = ProdutoDao.class)
public class ProdutoDaoImpl implements ProdutoDao{
    @Reference
    Fabrica f;


    @Override
    public void add(Produto produto){
        try(Connection cn = this.f.getConnection()){
            String sql = "INSERT INTO produto VALUES (?,?,?,?)";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setInt(1,produto.getID());
                ps.setString(2,produto.getNome());
                ps.setString(3,produto.getCategoria());
                ps.setDouble(4,produto.getPreco());

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel salvar nota no BD (core/dao/ProdutoDaoImpl/add())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/add())\n"+e.getMessage());
        }
    }

    @Override
    public Collection<Produto> getAll(String metodo){
        Collection<Produto> result = new ArrayList<Produto>();
        try(Connection cn = this.f.getConnection()){
            String sql = "SELECT * FROM produto";
            if(metodo!=null){
                switch(metodo) {
                    case "menor":
                        sql+=" ORDER BY preco ASC";
                        break;
                    case "maior":
                        sql+=" ORDER BY preco DESC";
                        break;
                    case "alfabetica":
                        sql+=" ORDER BY nome";
                        break;
                    default:
                        break;
                }
            }

            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()){
                    Produto p = new Produto(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4));
                    result.add(p);
                }
                return result;
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel obter valores do BD (core/dao/ProdutoDaoImpl/getAll())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/getAll())\n"+e.getMessage());
        }
    }

    @Override
    public Collection <Produto> pesquisa(String nome, String metodo){
        try(Connection cn = this.f.getConnection()){
            Collection<Produto> result = new ArrayList<Produto>();
            String sql = "SELECT * FROM produto WHERE NOME="+nome;
            if(metodo!=null){
                switch(metodo) {
                    case "menor":
                        sql+=" ORDER BY preco ASC";
                        break;
                    case "maior":
                        sql+=" ORDER BY preco DESC";
                        break;
                    case "alfabetica":
                        sql+=" ORDER BY nome";
                        break;
                    default:
                        break;
                }
            }
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()){
                    result.add(new Produto(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4)));
                }
                return result;
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel obter valores do BD (core/dao/ProdutoDaoImpl/pesquisar())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/pesquisar())\n"+e.getMessage());
        }
    }

    @Override
    public void updateName(int id,String nome){
        try(Connection cn = this.f.getConnection()){
            String sql = "UPDATE produto SET NOME = ? WHERE ID = ?";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setString(1,nome);
                ps.setInt(2,id);

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel realizar alteracao no BD (core/dao/ProdutoDaoImpl/updateName())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/updateName())\n"+e.getMessage());
        }
    }

    @Override
    public void updateCategoria(int id,String categoria){
        try(Connection cn = this.f.getConnection()){
            String sql = "UPDATE produto SET CATEGORIA = ? WHERE ID = ?";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setString(1,categoria);
                ps.setInt(2,id);

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel realizar alteracao no BD (core/dao/ProdutoDaoImpl/updateCategoria())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/updateCategoria())\n"+e.getMessage());
        }
    }

    @Override
    public void updatePreco(int id, double preco){
        try(Connection cn = this.f.getConnection()){
            String sql = "UPDATE produto SET PRECO = ? WHERE ID = ?";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setDouble(1,preco);
                ps.setInt(2,id);

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel realizar alteracao no BD (core/dao/ProdutoDaoImpl/updatePreco())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/updatePreco())\n"+e.getMessage());
        }
    }

    @Override
    public void updateAll(int id,String nome,String categoria,double preco){
        if(nome!=null){
            updateName(id,nome);
        }
        if(categoria!=null) {
            updateCategoria(id, categoria);
        }
        if(preco<=0){
            updatePreco(id,preco);
        }

    }

    @Override
    public void delete(int id){
        try(Connection cn = this.f.getConnection()){
            String sql = "DELETE FROM produto WHERE ID = ?";
            try(PreparedStatement ps = cn.prepareStatement(sql)){
                ps.setInt(1,id);

                ps.execute();
            }catch(Exception e){
                throw new RuntimeException("ERRO! Nao foi possivel deletar valor no BD (core/dao/ProdutoDaoImpl/delete())\n"+e.getMessage());
            }
        }catch(SQLException e){
            throw new RuntimeException("ERRO DE CONEXAO COM O BD (core/dao/ProdutoDaoImpl/delete())\n"+e.getMessage());
        }
    }
}