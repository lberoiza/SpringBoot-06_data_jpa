package com.springboot.app.models.dao;

import java.util.List;
import java.util.Optional;

import com.springboot.app.models.entity.Client;

public interface IClientDao {

  public List<Client> findAll();
  public void save(Client client);
  public Optional<Client> findById(Long id);
  
}
