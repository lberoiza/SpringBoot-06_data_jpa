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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  public String createClient(@Valid Client client, BindingResult result, Model model, RedirectAttributes flash,
      SessionStatus status) {

    if (result.hasErrors()) {
      model.addAttribute("title", "Create Client");
      model.addAttribute("error", "Please full all fields.");
      return "client/form";
    }

    String operationTypString = client.hasValidId() ? "updated" : "created";

    clientService.saveOrUpdate(client);
    status.setComplete();
    flash.addFlashAttribute("sucess", String.format("The Client was successful %s", operationTypString));
    return "redirect:/client/list";

  }

  @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
  public String editClient(@PathVariable Long id, Model model, RedirectAttributes flash) {
    Optional<Client> result = clientService.findById(id);

    if (result.isPresent()) {
      model.addAttribute("client", result.get());
      model.addAttribute("title", "Edit Client");
      return "client/form";
    }

    flash.addFlashAttribute("error", "The Client doesnt exist in der Database.");
    return "redirect:/client/list";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  public String deleteClient(@PathVariable Long id, RedirectAttributes flash) {
    clientService.delete(id);
    flash.addFlashAttribute("sucess", "The Client was successful deleted.");
    return "redirect:/client/list";
  }

}
