package com.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springboot.app.models.dao.IClientDao;

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

}
