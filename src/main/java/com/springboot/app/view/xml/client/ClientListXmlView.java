package com.springboot.app.view.xml.client;

import com.springboot.app.exporters.xml.wrappers.ClientList;
import com.springboot.app.models.entity.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.Map;

@Component("client/show_list.xml")
public class ClientListXmlView extends MarshallingView {

  @Autowired
  public ClientListXmlView(Jaxb2Marshaller marshaller) {
    super(marshaller);
  }

  @Override
  protected void renderMergedOutputModel(Map<String, Object> model,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

    @SuppressWarnings("unchecked")
    Page<Client> clients = (Page<Client>) model.get("clients");
    model.clear();
    model.put("clientList", new ClientList(clients.getContent()));
    super.renderMergedOutputModel(model, request, response);
  }
}
