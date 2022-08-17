package com.adobe.aem.guides.wknd.core.service;


import com.adobe.aem.guides.wknd.core.dao.NFDao;
import com.adobe.aem.guides.wknd.core.models.Cliente;
import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
import com.google.gson.Gson;
import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class ServicoNFImpl implements ServicoNF{
    @Reference
    private NFDao nfdao;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response){
        Collection<NotaFiscal> resultado = new ArrayList<NotaFiscal>();
        try{
            //Sei que não é o jeito mais bonito, mas é o mais pratico para o momento :/
            String resp = "<table style=\"width:100%\">\n" +
                    "  <tr>\n" +
                    "    <th>Numero</th>\n" +
                    "    <th>ID Cliente</th>\n" +
                    "    <th>ID Produto</th>\n" +
                    "    <th>Valor</th>\n" +
                    "  </tr>\n";

            if(request.getParameter("id")!=null){
                resultado=nfdao.getByID(Integer.parseInt(request.getParameter("id")));
            }else{
                resultado=nfdao.getAll();
            }

            for(NotaFiscal nf : resultado){
                resp+=" <tr>\n";
                resp+="   <td> "+nf.getNumero()+" </td>+\n";
                resp+="   <td> "+nf.getIdCliente()+" </td>+\n";
                resp+="   <td> "+nf.getIdProduto()+" </td>+\n";
                resp+="   <td> "+nf.getValor()+" </td>+\n";
                resp+=" </tr>\n";
            }
            resp+="</table>";
            response.getWriter().write(resp);
        }catch(Exception e ){
            System.out.println("ERRO no metodo doGet! "+e.getMessage());
        }

    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response){
        String stringRecebida = null;
        try {
            stringRecebida = IOUtils.toString(request.getReader());
            response.getWriter().write(stringRecebida);
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

        NotaFiscal converteNF = new NotaFiscal();
        try {
            for(String s : objetosseparados){
                //response.getWriter().write(s);
                converteNF = new Gson().fromJson(s, NotaFiscal.class);
                nfdao.add(converteNF);
            }

        }catch (Exception e){
            if(e.getMessage()!=null){
                try {
                    response.getWriter().write("ERRO AO RECEBER O JSON!" +e.getMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}


