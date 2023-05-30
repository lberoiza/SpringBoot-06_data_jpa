package com.springboot.app.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.app.exceptions.UserNotFoundException;



@ControllerAdvice
public class ErrorHandlerController {
  
  @ExceptionHandler(ArithmeticException.class)
  public String arithmeticError(ArithmeticException aEx, Model model) {
    model.addAttribute("error", "Arithmetical Error");
    model.addAttribute("message", aEx.getMessage());
    model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    model.addAttribute("timestamp", new Date());
    return "error/custom/arithmetic_exception";
  }
  
  
  @ExceptionHandler(NumberFormatException.class)
  public String numberFormatError(NumberFormatException nfEx, Model model) {
    model.addAttribute("error", "Number Format Exception");
    model.addAttribute("message", nfEx.getMessage());
    model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    model.addAttribute("timestamp", new Date());
    return "error/custom/number_format_exception";
  }
  

  @ExceptionHandler(UserNotFoundException.class)
  public String userNotFound(UserNotFoundException uNfEx, Model model) {
    model.addAttribute("title", "User Not in System");
    model.addAttribute("ex", uNfEx);
    return "error/custom/user_not_found";
  }
  
}
