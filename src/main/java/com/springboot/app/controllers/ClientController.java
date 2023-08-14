package com.springboot.app.controllers;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
import com.springboot.app.services.IClientService;
import com.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client")
@SessionAttributes("client")
public class ClientController {

  protected final Log log = LogFactory.getLog(this.getClass());

  private IClientService clientService;

  private IFileService fileService;

  private MessageSource messageSource;


  @Autowired
  public ClientController(IClientService clientService, IFileService fileService, MessageSource messageSource) {
    this.clientService = clientService;
    this.fileService = fileService;
    this.messageSource = messageSource;
  }

  @GetMapping(value = "/{id}")
  public String showClientDetails(@PathVariable(value = "id") Long clientId, Model model, RedirectAttributes flash) {

    Optional<Client> result = clientService.fetchClientByIdWithInvoices(clientId);

    if (result.isEmpty()) {
      flash.addFlashAttribute("error", String.format("The Client with id %d was not found", clientId));
      return "redirect:/client/list";
    }

    Client client = result.get();
    model.addAttribute("title", String.format("Details of '%s'", client.getFullName()));
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
    waysToGetRoles(authentication, httpServletRequest);

    Pageable pageable = PageRequest.of(page, 4);
    model.addAttribute("title",
        messageSource.getMessage("text.controller.client.clientlist.title", null, locale)
    );

    Page<Client> clients = clientService.findAll(pageable);
    model.addAttribute("clients", clients);

    PageRender<Client> pageRender = new PageRender<>("/client/list", clients);
    model.addAttribute("pageRender", pageRender);
    return "client/show_list";

  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public String showForm(Map<String, Object> model) {
    model.put("title", "Create Client");
    model.put("client", new Client());
    return "client/form";
  }

  @RequestMapping(value = "/form", method = RequestMethod.POST)
  public String createClient(@Valid Client client, BindingResult result, Model model, @RequestParam("file") MultipartFile image, RedirectAttributes flash,
                             SessionStatus status) {

    if (!image.isEmpty()) {
      try {
        String imageName = fileService.uploadImage(image);
        if (client.hasImage()) {
          fileService.deleteImage(client.getImage());
        }
        client.setImage(imageName);
        String successStr = String.format("The Image '%s' was successfully uploaded", imageName);
        flash.addFlashAttribute("info", successStr);
      } catch (IllegalArgumentException ex) {
        model.addAttribute("error", ex.getMessage());
      }
    }


    if (result.hasErrors()) {
      model.addAttribute("title", "Create Client");
      model.addAttribute("error", "Please full all fields.");
      return "client/form";
    }

    String operationTypString = client.hasValidId() ? "updated" : "created";

    client = clientService.saveOrUpdate(client);
    status.setComplete();
    flash.addFlashAttribute("success", String.format("The Client was successful %s", operationTypString));
    flash.addFlashAttribute("client", client);
    return "redirect:/client/" + client.getId();

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
    Optional<Client> result = clientService.findById(id);
    if (result.isPresent()) {
      Client client = result.get();
      if (fileService.deleteImage(client.getImage())) {
        flash.addFlashAttribute("info", "The Image of the Client was successful deleted.");
      }
      clientService.delete(id);
      flash.addFlashAttribute("success", "The Client was successful deleted.");
    } else {
      flash.addFlashAttribute("error", "The Client was not found in the BD.");
    }
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

  private void waysToGetRoles(
      Authentication authentication,
      HttpServletRequest httpServletRequest) {

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
