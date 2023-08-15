package com.springboot.app.controllers;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.springboot.app.services.ClientService;
import com.springboot.app.services.IFileService;
import com.springboot.app.util.paginator.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.entity.Client;
import com.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client")
@SessionAttributes("client")
public class ClientController {

  protected final Log log = LogFactory.getLog(this.getClass());

  private final ClientService clientService;

  private final IFileService fileService;

  private final MessageSource messageSource;


  @Autowired
  public ClientController(ClientService clientService,
                          IFileService fileService,
                          MessageSource messageSource) {
    this.clientService = clientService;
    this.fileService = fileService;
    this.messageSource = messageSource;
  }

  @GetMapping(value = "/{id}")
  public String showClientDetails(@PathVariable(value = "id") Long clientId,
                                  Model model,
                                  RedirectAttributes flash,
                                  Locale locale) {

    Optional<Client> result = clientService.fetchClientByIdWithInvoices(clientId);

    if (result.isEmpty()) {
      String errorStr = this.messageSource.getMessage("text.client.flash.db.error", null, locale);
      flash.addFlashAttribute("error", String.format(errorStr, clientId));
      return "redirect:/client/list";
    }

    Client client = result.get();
    String titleStr = this.messageSource.getMessage("text.client.details.title", null, locale);
    model.addAttribute("title", String.format(titleStr, client.getFullName()));
    model.addAttribute("client", client);
    return "client/show_details";
  }

  //  @Secured({"ROLE_ADMIN"})
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String getList(
      @RequestParam(name = "page", defaultValue = "0") int page,
      Model model,
      Authentication authentication,
      HttpServletRequest httpServletRequest,
      Locale locale) {

    waysToValidateUser(authentication);
    waysToGetRoles(httpServletRequest);

    Pageable pageable = PageRequest.of(page, 4);
    String title = this.messageSource.getMessage("text.client.clientlist.title", null, locale);
    model.addAttribute("title", title);

    Page<Client> clients = clientService.findAll(pageable);
    model.addAttribute("clients", clients);

    PageRender<Client> pageRender = new PageRender<>("/client/list", clients);
    model.addAttribute("pageRender", pageRender);
    return "client/show_list";

  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public String showForm(Map<String, Object> model, Locale locale) {
    String title = this.messageSource.getMessage("text.client.new", null, locale);
    model.put("title", title);
    model.put("client", new Client());
    return "client/form";
  }

  @RequestMapping(value = "/form", method = RequestMethod.POST)
  public String createClient(@Valid Client client,
                             BindingResult result,
                             Model model,
                             @RequestParam("file") MultipartFile image,
                             RedirectAttributes flash,
                             SessionStatus status,
                             Locale locale) {

    if (!image.isEmpty()) {
      String messageInfo = this.messageSource.getMessage("text.client.flash.foto.upload.success", null, locale);
      try {
        String imageName = fileService.uploadImage(image);
        if (client.hasImage()) {
          fileService.deleteImage(client.getImage());
        }
        client.setImage(imageName);
        String successStr = String.format(messageInfo, imageName);
        flash.addFlashAttribute("info", successStr);
      } catch (IllegalArgumentException ex) {
        model.addAttribute("error", ex.getMessage());
      }
    }


    if (result.hasErrors()) {
      String titleStr = this.messageSource.getMessage("text.client.new", null, locale);
      String errorStr = this.messageSource.getMessage("text.client.form.error", null, locale);
      model.addAttribute("title", titleStr);
      model.addAttribute("error", errorStr);
      return "client/form";
    }

    String operationMessage = client.hasValidId() ? "text.client.flash.edit.success" : "text.client.flash.new.success";
    String successMessage = this.messageSource.getMessage(operationMessage, null, locale);

    client = clientService.saveOrUpdate(client);
    status.setComplete();
    flash.addFlashAttribute("success", successMessage);
    flash.addFlashAttribute("client", client);
    return "redirect:/client/" + client.getId();

  }

  @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
  public String editClient(
      @PathVariable Long id,
      Model model,
      RedirectAttributes flash,
      Locale locale) {
    Optional<Client> result = clientService.findById(id);

    if (result.isPresent()) {
      String title = this.messageSource.getMessage("text.client.form.title.edit", null, locale);
      model.addAttribute("title", title);
      model.addAttribute("client", result.get());
      return "client/form";
    }

    String error = this.messageSource.getMessage("text.client.flash.db.error", null, locale);
    flash.addFlashAttribute("error", String.format(error, id));
    return "redirect:/client/list";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  public String deleteClient(@PathVariable Long id,
                             RedirectAttributes flash,
                             Locale locale) {
    Optional<Client> clientOptional = clientService.findById(id);

    clientOptional.ifPresentOrElse(
        client -> {
          if (fileService.deleteImage(client.getImage())) {
            String successImageDeletion = this.messageSource.getMessage("text.client.flash.foto.delete.success", null, locale);
            flash.addFlashAttribute("info", String.format(successImageDeletion, client.getImage()));
          }
          clientService.deleteById(id);
          String successClientDeletion = this.messageSource.getMessage("text.client.flash.delete.success", null, locale);
          flash.addFlashAttribute("success", successClientDeletion);
        },
        () -> {
          String errorCustomerDeletion = this.messageSource.getMessage("text.client.flash.db.error", null, locale);
          flash.addFlashAttribute("error", String.format(errorCustomerDeletion, id));
        }
    );
    return "redirect:/client/list";
  }


  private void waysToValidateUser(Authentication authentication) {

    // 1. Método de Inyeccion de Dependencias
    if (authentication != null) {
      log.info("Show List of User to: " + authentication.getName());
    }

    // 2. Método Usando Application Context
    Optional<Authentication> optionalAuthentication = AuthenticationUtils.getAutentication();
    optionalAuthentication.ifPresent(auth -> log.info("From Application Context Show List of User to: " + auth.getName()));
  }

  private void waysToGetRoles(HttpServletRequest httpServletRequest) {

    // 1. Método Usando Application Context
    if (AuthenticationUtils.hasRole("ROLE_ADMIN")) {
      log.info("Usando Application Context, Tienes el rol de ADMIN");
    }

    // 2. Método Usando SecurityContextHolderAwareRequestWrapper
    SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(httpServletRequest, "ROLE_");
    if (securityContext.isUserInRole("ADMIN")) {
      log.info("Usando SecurityContextHolderAwareRequestWrapper, Tienes el rol de ADMIN");
    }

    // 3. Método Usando HttpServletRequest
    if (httpServletRequest.isUserInRole("ROLE_ADMIN")) {
      log.info("Usando HttpServletRequest, Tienes el rol de ADMIN");
    }
  }


}
