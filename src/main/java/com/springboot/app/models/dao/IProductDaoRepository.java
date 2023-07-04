package com.springboot.app.models.dao;

import com.springboot.app.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductDaoRepository extends JpaRepository<Product, Long>{

  @Query("select p from Product p where p.name like %?1%")
  public List<Product> findByName(String name);


//   Otra Opcion: usando convencion de nombres para generar busquedas
//  public List<Product> findByNameLikeIgnoreCase(String name);


}
