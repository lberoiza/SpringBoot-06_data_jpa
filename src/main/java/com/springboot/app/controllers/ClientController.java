package com.springboot.app.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.springboot.app.models.entity.Client;
import com.springboot.app.services.IClientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client")
@SessionAttributes("client")
public class ClientController {

  @Autowired
  private IClientService clientService;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String getList(Model model) {
    model.addAttribute("title", "Client List");
    model.addAttribute("clients", clientService.findAll());
    return "client/show_list";

  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public String showForm(Map<String, Object> model) {
    model.put("title", "Create Client");
    model.put("client", new Client());
    return "client/form";
  }

  @RequestMapping(value = "/form", method = RequestMethod.POST)
  public String createClient(@Valid Client client, BindingResult result, Model model, SessionStatus status) {

    if (result.hasErrors()) {
      model.addAttribute("title", "Create Client");
      return "client/form";
    }

    clientService.saveOrUpdate(client);
    status.setComplete();
    return "redirect:/client/list";

  }

  @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
  public String editClient(@PathVariable Long id, Model model) {
    Optional<Client> result = clientService.findById(id);

    if (result.isPresent()) {
      model.addAttribute("client", result.get());
      model.addAttribute("title", "Edit Client");
      return "client/form";
    }

    return "redirect:/client/list";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  public String editClient(@PathVariable Long id) {
    clientService.delete(id);
    return "redirect:/client/list";
  }

}
