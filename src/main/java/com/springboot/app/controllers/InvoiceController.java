package com.springboot.app.controllers;

import com.springboot.app.models.entity.Client;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import com.springboot.app.models.entity.Product;
import com.springboot.app.services.IClientService;
import com.springboot.app.services.IInvoiceService;
import com.springboot.app.services.IProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoiceController {

  private final Logger logger = LoggerFactory.getLogger(getClass());


  private final IClientService clientService;

  private final IProductService productService;

  private final IInvoiceService invoiceService;


  public InvoiceController(@Autowired IClientService clientService,
                           @Autowired IProductService productService,
                           @Autowired IInvoiceService invoiceService) {
    this.clientService = clientService;
    this.productService = productService;
    this.invoiceService = invoiceService;
  }


  @GetMapping("/{invoiceId}")
  public String showInvoice(@PathVariable(name = "invoiceId") Long invoiceId, Model model, RedirectAttributes flash) {
    Optional<Invoice> optionalInvoice = this.invoiceService.fetchInvoiceWithClientWithInvoiceItemsWithProduct(invoiceId);

    if (optionalInvoice.isEmpty()) {
      String error = String.format("The Invoice (%d) was not found in System.", invoiceId);
      flash.addFlashAttribute("error", error);
      return "redirect:/client/list";
    }

    Invoice invoice = optionalInvoice.get();

    String title = String.format("Invoice nr: %d of %s", invoice.getId(), invoice.getClientFullName());
    model.addAttribute("title", title);
    model.addAttribute("invoice", invoice);
    return "invoice/show_details";
  }


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

    String title = String.format("New Invoice of %s", invoice.getClientFullName());
    model.addAttribute("title", title);
    model.addAttribute("invoice", invoice);

    return "invoice/form";
  }


  @PostMapping("/form")
  public String saveInvoice(@Valid Invoice invoice,
                            BindingResult validation,
                            Model model,
                            @RequestParam(name = "product_id[]", required = false) Long[] productIds,
                            @RequestParam(name = "product_quantity[]", required = false) Integer[] productQuantities,
                            RedirectAttributes flash,
                            SessionStatus status) {

    if (validation.hasErrors()) {
      String title = String.format("New Invoice of %s", invoice.getClientFullName());
      model.addAttribute("title", title);
      return "invoice/form";
    }


    if (productIds == null || productIds.length == 0) {
      String title = String.format("New Invoice of %s", invoice.getClientFullName());
      model.addAttribute("title", title);
      model.addAttribute("error", "There are not products in the invoice.");
      return "invoice/form";
    }


    for (int i = 0; i < productIds.length; i++) {
      Long productId = productIds[i];
      Integer quantity = productQuantities[i];
      Product product = null;
      String productName = "product not found";

      Optional<Product> optionalProduct = productService.findById(productIds[i]);
      if (optionalProduct.isPresent()) {
        product = optionalProduct.get();
        productName = product.getName();
      }

      InvoiceItem invoiceItem = new InvoiceItem();
      invoiceItem.setProduct(product);
      invoiceItem.setQuantity(quantity);
      invoice.addInvoiceItem(invoiceItem);
      String infoStr = String.format("Product (%d) \"%s\" with quantity: %d added to invoice (%d)", productId, productName, quantity, invoice.getId());
      logger.info(infoStr);
    }

    this.invoiceService.saveInvoice(invoice);
    status.setComplete();

    String flashString = String.format("The Invoice (%s) was successfully created for %s", invoice.getId(), invoice.getClientFullName());
    flash.addFlashAttribute("success", flashString);

    return "redirect:/client/" + invoice.getClientId();
  }

  @GetMapping("delete/{invoiceId}")
  public String deleteInvoice(@PathVariable Long invoiceId, RedirectAttributes flash) {
    Optional<Invoice> optionalInvoice = this.invoiceService.findById(invoiceId);

    if (optionalInvoice.isEmpty()) {
      String errorStr = String.format("The Invoice (%s) is not in system", invoiceId);
      flash.addFlashAttribute("error", errorStr);
      return "redirect:/client/list";
    }

    Invoice invoice = optionalInvoice.get();
    this.invoiceService.deleteById(invoiceId);

    String successStr = String.format("The Invoice (%d) \"%s\" of %s was deleted.", invoice.getId(), invoice.getDescription(), invoice.getClientFullName());
    flash.addFlashAttribute("success", successStr);
    return "redirect:/client/" + invoice.getClientId();
  }

}
