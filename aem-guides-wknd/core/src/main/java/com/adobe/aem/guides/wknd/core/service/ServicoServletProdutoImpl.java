package com.adobe.aem.guides.wknd.core.service;

import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.Produto;
import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;

@Component(immediate=true,service= ServicoServletProduto.class)
public class ServicoServletProdutoImpl implements ServicoServletProduto {

    @Reference
    ProdutoDao produto;

    //Farei tratamento de strings ao inves de mapeamento de json,
    // uma vez que tenho maior facilidade com o primeiro

    private String[] separaJson(String s){
        String[] objetosseparados= new String[10];
        int pos=0;
        objetosseparados[pos]="";
        for(int i=0;i<s.length();i++){
            objetosseparados[pos]+=s.charAt(i);
            if(s.charAt(i)=='}'){
                objetosseparados[pos]+=" "+ pos;
                pos++;
            }

        }
        return objetosseparados;
        //fim do metodo horroroso do Davi pra separar os objetos json
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response){
        String stringRecebida = null;
        try {
            stringRecebida = IOUtils.toString(request.getReader());
            //response.getWriter().write(stringRecebida);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Farei tratamento de string aqui
        String[] objetosseparados= new String[999];
        int pos=0;
        for(int i=0;i<stringRecebida.length();i++){
            if(stringRecebida.charAt(i)=='{'){
                objetosseparados[pos]="";
                do{
                    objetosseparados[pos]+=stringRecebida.charAt(i);
                    i++;
                }while(stringRecebida.charAt(i-1)!='}');
                pos++;
            }
        }
        //fim do metodo horroroso do Davi pra separar os objetos json
        //OBS.: Aparentemente se transformar o tratamento acima em um metodo, o meu AEM buga,
        // então perdoe o crime contra as boas práticas da POO

        Produto converteProduto = new Produto();
        try {
            for(String s : objetosseparados){
                //response.getWriter().write(s);
                converteProduto = new Gson().fromJson(s, Produto.class);
                produto.add(converteProduto);
            }

        }catch (Exception e){
            if(e.getMessage()!=null){
                try {
                    response.setContentType("application/json");
                    response.getWriter().write(new Gson().toJson("ERRO AO RECEBER O JSON!" +e.getMessage()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    @Override
    public void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp){
        resp.setContentType("application/json");
        try{
                    if(req.getParameter("nome")!=null){
                        String resposta = new Gson().toJson(produto.pesquisa(req.getParameter("nome"),req.getParameter("metodo")));
                        resp.getWriter().write(resposta);
                    }else{
                        String resposta = new Gson().toJson(produto.getAll(req.getParameter("metodo")));
                        resp.getWriter().write(resposta);
                    }
        }catch (Exception e) {
            try {
                resp.setContentType("application/json");
                resp.getWriter().write(new Gson().toJson("ERRO NO METODO doGet()!" +e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void doDelete(SlingHttpServletRequest req, SlingHttpServletResponse resp) {
        try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    produto.delete(id);
        } catch (Exception e) {
            try {
                resp.setContentType("application/json");
                resp.getWriter().write(new Gson().toJson("ERRO AO DELETAR OBJETO!" +e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    @Override
    public void doPut(SlingHttpServletRequest req, SlingHttpServletResponse resp){
        try{
            int id = Integer.parseInt(req.getParameter("id"));
            produto.updateAll(id,req.getParameter("nome"),req.getParameter("categoria"),Double.parseDouble(req.getParameter("preco")));

        }catch(Exception e){
            try {
                resp.setContentType("application/json");
                resp.getWriter().write(new Gson().toJson("ERRO AO ATUALIZAR OBJETO!" +e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}

