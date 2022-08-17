package com.adobe.aem.guides.wknd.core.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

public interface ServicoNF {
    void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response);

}
