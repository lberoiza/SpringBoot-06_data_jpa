package com.springboot.app.services;

import com.springboot.app.models.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

  public List<Product> findByName(String name);

  public Optional<Product> findById(Long id);

}
