package com.springboot.app.controllers;

import com.springboot.app.models.entity.Client;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import com.springboot.app.models.entity.Product;
import com.springboot.app.services.ClientService;
import com.springboot.app.services.InvoiceService;
import com.springboot.app.services.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoiceController {

  private final Logger logger = LoggerFactory.getLogger(getClass());


  private final ClientService clientService;

  private final ProductService productService;

  private final InvoiceService invoiceService;

  private final MessageSource messageSource;

  @Autowired
  public InvoiceController(ClientService clientService,
                           ProductService productService,
                           InvoiceService invoiceService,
                           MessageSource messageSource) {
    this.clientService = clientService;
    this.productService = productService;
    this.invoiceService = invoiceService;
    this.messageSource = messageSource;
  }


  @GetMapping("/{invoiceId}")
  public String showInvoice(@PathVariable(name = "invoiceId") Long invoiceId,
                            Model model,
                            RedirectAttributes flash,
                            Locale locale) {
    Optional<Invoice> optionalInvoice = this.invoiceService.fetchInvoiceWithClientWithInvoiceItemsWithProduct(invoiceId);

    if (optionalInvoice.isEmpty()) {
      String messageStr = this.messageSource.getMessage("text.invoice.flash.db.error", null, locale);
      String error = String.format(messageStr, invoiceId);
      flash.addFlashAttribute("error", error);
      return "redirect:/client/list";
    }

    Invoice invoice = optionalInvoice.get();

    String titleStr = this.messageSource.getMessage("text.invoice.show.title", null, locale);
    String title = String.format(titleStr, invoice.getId(), invoice.getClientFullName());
    model.addAttribute("title", title);
    model.addAttribute("invoice", invoice);
    return "invoice/show_details";
  }


  @GetMapping("/form/{clientId}")
  public String createInvoice(@PathVariable(value = "clientId") Long clientId,
                              Model model,
                              RedirectAttributes flash,
                              Locale locale) {

    Optional<Client> clientOptional = clientService.findById(clientId);

    if (clientOptional.isEmpty()) {
      String errorMessage = this.messageSource.getMessage("text.client.flash.db.error", null, locale);
      String error = String.format(errorMessage, clientId);
      flash.addFlashAttribute("error", error);
      return "redirect:/client/list";
    }

    Invoice invoice = new Invoice();
    invoice.setClient(clientOptional.get());

    String newInvoceStr = this.messageSource.getMessage("text.invoice.form.title", null, locale);
    String title = String.format(newInvoceStr, invoice.getClientFullName());
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
                            SessionStatus status,
                            Locale locale) {

    String newInvoceStr = this.messageSource.getMessage("text.invoice.form.title", null, locale);
    String title = String.format(newInvoceStr, invoice.getClientFullName());

    if (validation.hasErrors()) {
      model.addAttribute("title", title);
      return "invoice/form";
    }


    if (productIds == null || productIds.length == 0) {
      String notProductStr = this.messageSource.getMessage("text.invoice.show.no.position", null, locale);
      model.addAttribute("title", title);
      model.addAttribute("error", notProductStr);
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

    this.invoiceService.saveOrUpdate(invoice);
    status.setComplete();

    String successStr = this.messageSource.getMessage("text.invoice.flash.new.success", null, locale);
    String flashString = String.format(successStr, invoice.getId(), invoice.getClientFullName());
    flash.addFlashAttribute("success", flashString);

    return "redirect:/client/" + invoice.getClientId();
  }

  @GetMapping("delete/{invoiceId}")
  public String deleteInvoice(@PathVariable Long invoiceId,
                              RedirectAttributes flash,
                              Locale locale) {
    Optional<Invoice> optionalInvoice = this.invoiceService.findById(invoiceId);

    if (optionalInvoice.isEmpty()) {
      String messageStr = this.messageSource.getMessage("text.invoice.flash.db.error", null, locale);
      String errorStr = String.format(messageStr, invoiceId);
      flash.addFlashAttribute("error", errorStr);
      return "redirect:/client/list";
    }

    Invoice invoice = optionalInvoice.get();
    this.invoiceService.deleteById(invoiceId);

    String successMessage = this.messageSource.getMessage("text.invoice.flash.delete.success", null, locale);
    String successStr = String.format(successMessage, invoice.getId(), invoice.getClientFullName());
    flash.addFlashAttribute("success", successStr);
    return "redirect:/client/" + invoice.getClientId();
  }

}
