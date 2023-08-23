package com.springboot.app.view.json.client;

import com.springboot.app.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@Component("client/show_list.json")
public class ClientListJsonView extends MappingJackson2JsonView {

  @Override
  protected Object filterModel(Map<String, Object> model) {

    @SuppressWarnings("unchecked")
    Page<Client> clients = (Page<Client>) model.get("clients");
    model.clear();
    model.put("clients", clients.getContent());
    return super.filterModel(model);
  }
}
