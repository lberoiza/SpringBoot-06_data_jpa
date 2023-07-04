package com.springboot.app.controllers;

import com.springboot.app.models.entity.Product;
import com.springboot.app.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

  private final IProductService productService;

  public ProductController(@Autowired IProductService pService){
    this.productService = pService;
  }

  @GetMapping(value = "/find_by_name/{productName}", produces = {"application/json"})
  public @ResponseBody List<Product> findByName(@PathVariable(name = "productName") String productName){
    return productService.findByName(productName);
  }
}
