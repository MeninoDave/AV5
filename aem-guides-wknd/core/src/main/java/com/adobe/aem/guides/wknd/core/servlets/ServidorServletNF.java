/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.dao.NFDao;
import com.adobe.aem.guides.wknd.core.models.NotaFiscal;
import com.adobe.aem.guides.wknd.core.service.ServicoNF;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.tika.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(immediate = true, service = Servlet.class, property = {
        SLING_SERVLET_METHODS + "=" + "GET",
        SLING_SERVLET_PATHS + "=" + "/bin/keepalive/avaliacao/notaFiscal",
        SLING_SERVLET_EXTENSIONS + "=" + "txt",SLING_SERVLET_EXTENSIONS + "=" + "html"})

@ServiceDescription("Servlet de Notas Fiscais")
public class ServidorServletNF extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    NFDao nfdao;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        //servico.doGet(req,resp);
        resp.setContentType("text/html");
        Collection<NotaFiscal> resultado = new ArrayList<NotaFiscal>();
        try{
            //Sei que não é o jeito mais bonito, mas é o mais pratico para o momento :/
            String resposta = "<table style=\"width:100%\">\n" +
                    "  <tr>\n" +
                    "    <th>Numero</th>\n" +
                    "    <th>ID Cliente</th>\n" +
                    "    <th>ID Produto</th>\n" +
                    "    <th>Valor</th>\n" +
                    "  </tr>\n";

            if(req.getParameter("id")!=null){ //idcliente
                resultado=nfdao.getByID(Integer.parseInt(req.getParameter("id")));
            }else{
                resultado=nfdao.getAll();
            }

            for(NotaFiscal nf : resultado){
                resposta+=" <tr>\n";
                resposta+="   <td> "+nf.getNumero()+" </td>\n";
                resposta+="   <td> "+nf.getIdCliente()+" </td>\n";
                resposta+="   <td> "+nf.getIdProduto()+" </td>\n";
                resposta+="   <td> "+nf.getValor()+" </td>\n";
                resposta+=" </tr>\n";
            }
            resposta+="</table>";
            resp.getWriter().write(resposta);
        }catch(Exception e ){
            try {
                resp.setContentType("application/json");
                resp.getWriter().write(new Gson().toJson("ERRO NO METODO doGet()!" +e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws ServletException, IOException {
        String stringRecebida = null;
        try {
            stringRecebida = IOUtils.toString(request.getReader());
            response.getWriter().write(stringRecebida);
        } catch (IOException e) {
            try {
                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson("ERRO NO METODO doPost()!" +e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
                    response.setContentType("application/json");
                    response.getWriter().write(new Gson().toJson("ERRO AO RECEBER O JSON!" +e.getMessage()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }
}


