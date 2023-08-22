package com.springboot.app.exporters.xml.wrappers;

import com.springboot.app.models.entity.Client;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


import java.util.List;

@XmlRootElement(name = "clients")
public class ClientList {

  @XmlElement(name = "client")
  public List<Client> clientList;

  public ClientList() {
  }

  public ClientList(List<Client> clientList) {
    this.clientList = clientList;
  }

  public List<Client> getClientList() {
    return clientList;
  }
}
