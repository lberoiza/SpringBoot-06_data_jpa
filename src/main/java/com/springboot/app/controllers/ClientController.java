package com.springboot.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springboot.app.models.dao.IClientDao;
import com.springboot.app.models.entity.Client;

@Controller
@RequestMapping("/client")
public class ClientController {

  @Autowired
  private IClientDao clientDao;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String getList(Model model) {
    model.addAttribute("title", "Client List");
    model.addAttribute("clients", clientDao.findAll());
    return "client/show_list";

  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public String showForm(Map<String, Object> model) {
    model.put("title", "Create Client");
    model.put("client", new Client());
    return "client/form";
  }

  @RequestMapping(value = "/form", method = RequestMethod.POST)
  public String createClient(Client client) {
    clientDao.save(client);
    return "redirect:/client/list";

  }

}
