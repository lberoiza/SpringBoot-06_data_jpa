package com.springboot.app.restcontrollers;

import com.springboot.app.exporters.xml.wrappers.ClientList;
import com.springboot.app.models.entity.Client;
import com.springboot.app.services.ClientService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientRestController {


  protected final Log log = LogFactory.getLog(this.getClass());

  private final ClientService clientService;

  @Autowired
  public ClientRestController(ClientService clientService) {
    this.clientService = clientService;
  }

  @GetMapping(value = "/list")
  public ClientList getClientList(){
    List<Client> clients = clientService.findAll();
    return new ClientList(clients);
  }


}
