package com.adobe.aem.guides.wknd.core.service;

import com.google.gson.Gson;
//MODELS
import com.adobe.aem.guides.wknd.core.models.Cliente;
//DAO
import com.adobe.aem.guides.wknd.core.dao.ClienteDao;
import com.adobe.aem.guides.wknd.core.dao.ProdutoDao;
//SLING
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
//OSGi
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
//IOUtils
import org.apache.tika.io.IOUtils;
//JAVA.IO
import java.io.IOException;

@Component(immediate=true,service= ServicoServletCliente.class)
public class ServicoServletClienteImpl implements ServicoServletCliente {

    @Reference
    ClienteDao cliente;

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

        Cliente converteCliente = new Cliente();
        try {

            for(String s : objetosseparados){
                converteCliente = new Gson().fromJson(s, Cliente.class);
                cliente.add(converteCliente);
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
                        String resposta = new Gson().toJson(cliente.pesquisa(req.getParameter("nome")));
                        resp.getWriter().write(resposta);
                    }else{
                        String resposta = new Gson().toJson(cliente.getAll());
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
                    cliente.delete(id);

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
    public void doPut(SlingHttpServletRequest req, SlingHttpServletResponse resp){//Parametros: (nome)antigo e (nome)novo
        try{
            Cliente cl = cliente.pesquisa(req.getParameter("antigo"));
            cliente.update(cl,req.getParameter("novo"));
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

