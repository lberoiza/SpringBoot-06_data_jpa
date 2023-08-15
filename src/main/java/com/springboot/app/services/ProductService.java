package com.springboot.app.services;

import com.springboot.app.models.dao.IProductDaoRepository;
import com.springboot.app.models.entity.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Service("ProductService")
public class ProductService extends BaseCRUDService<Product, IProductDaoRepository>{


  @Transactional(readOnly = true)
  public List<Product> findByName(String name) {
    return repository.findByName(name);
  }

  @Transactional(readOnly = true)
  public Optional<Product> findById(Long id) {
    return repository.findById(id);
  }
}
