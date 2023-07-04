package com.springboot.app.services;

import com.springboot.app.models.dao.IProductDaoRepository;
import com.springboot.app.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Service("ProductService")
public class ProductService implements IProductService{

  private final IProductDaoRepository productDao;


  public ProductService(@Autowired IProductDaoRepository pDao){
    this.productDao = pDao;
  }


  @Override
  @Transactional(readOnly = true)
  public List<Product> findByName(String name) {
    return productDao.findByName(name);
  }
}
