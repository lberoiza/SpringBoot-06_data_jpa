package com.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
public class LoginController {

  @Autowired
  private final MessageSource messageSource;

  public LoginController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @GetMapping("/login")
  public String login(
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "logout", required = false) String logout,
      Model model,
      Principal principal,
      RedirectAttributes flash,
      Locale locale) {
    if (principal != null) {
      String info = this.messageSource.getMessage("text.login.already", null, locale);
      flash.addFlashAttribute("info", info);
      return "redirect:" + MainControler.START_POINT;
    }

    if (error != null) {
      error = this.messageSource.getMessage("text.login.error", null, locale);
      model.addAttribute("error", error);
    }

    if (logout != null) {
      logout = this.messageSource.getMessage("text.login.logout", null, locale);
      model.addAttribute("success", logout);
    }

    return "login/login_page";
  }

}
