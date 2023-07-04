package com.springboot.app.services;

import com.springboot.app.models.entity.Product;

import java.util.List;

public interface IProductService {

  public List<Product> findByName(String name);

}
