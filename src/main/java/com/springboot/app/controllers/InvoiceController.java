package com.springboot.app.controllers;

import com.springboot.app.models.entity.Client;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

  @Autowired
  private IClientService clientService;

  @GetMapping("/form/{clientId}")
  public String createInvoice(@PathVariable(value = "clientId") Long clientId,
                              Model model,
                              RedirectAttributes flash) {

    Optional<Client> clientOptional = clientService.findById(clientId);

    if (clientOptional.isEmpty()) {
      String error = String.format("The Client (%d) was not found in System.", clientId);
      flash.addFlashAttribute("error", error);
      return "redirect:/client/list";
    }

    Invoice invoice = new Invoice();
    invoice.setClient(clientOptional.get());

    String title = String.format("New Invoice of %s", invoice.getClient().getFullName());
    model.addAttribute("title", title);
    model.addAttribute("invoice", invoice);

    return "invoice/form";
  }

}
