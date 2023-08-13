package com.springboot.app.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {

  @GetMapping("/locale")
  public String setLanguage(HttpServletRequest servletRequest) {
    String lastPart = servletRequest.getHeader("referer");
    return "redirect:".concat(lastPart);
  }
}
