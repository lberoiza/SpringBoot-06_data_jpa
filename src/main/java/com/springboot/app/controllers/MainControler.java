package com.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainControler {

  @GetMapping("/")
  public String startPoint(){
    return "redirect:/client/list";
  }

}
