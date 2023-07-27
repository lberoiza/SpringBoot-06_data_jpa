package com.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainControler {

  public static final String START_POINT = "/client/list";


  @GetMapping("/")
  public String startPoint(){
    return "redirect:" + START_POINT;
  }

}
