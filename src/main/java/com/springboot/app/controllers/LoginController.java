package com.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String login(Model model, Principal principal, RedirectAttributes flash){
    if(principal != null ) {
      String info = "You are already logged";
      flash.addFlashAttribute("info", info);
      return "redirect:/";
    }

    return "login/login_page";
  }

}
