package com.springboot.app.controllers;

import java.util.Map;
import java.util.Optional;

import com.springboot.app.services.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  @Autowired
  private IClientService clientService;

  @Autowired
  private IUploadFileService uploadFileService;


  @GetMapping(value="/{id}")
  public String showClientDetails(@PathVariable(value = "id") Long clientId, Model model, RedirectAttributes flash){

    Optional<Client> result = clientService.findById(clientId);

    if(result.isEmpty()){
      flash.addFlashAttribute("error", String.format("The Client with id %d was not found", clientId));
      return "redirect:/client/list";
    }

    Client client = result.get();
    model.addAttribute("title", String.format("Details of '%s'", client.getFullName()));
    model.addAttribute("client", client);
    return "client/show_details";
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String getList(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

    Pageable pageable = PageRequest.of(page, 4);
    model.addAttribute("title", "Client List");

    Page<Client> clients = clientService.findAll(pageable);
    model.addAttribute("clients", clients);

    PageRender<Client> pageRender = new PageRender<Client>("/client/list", clients);
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

    try{
      String imageName = image.getOriginalFilename();
      uploadFileService.uploadImage(image);
      client.setImage(imageName);
      String successStr = String.format("The Image '%s' was successfully uploaded", imageName);
      flash.addFlashAttribute("info", successStr);
    }catch(IllegalArgumentException ex) {
      model.addAttribute("error", ex.getMessage());
    }

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
