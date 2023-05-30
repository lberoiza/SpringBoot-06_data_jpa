package com.springboot.app.models.dao;

import java.util.Optional;

import com.springboot.app.models.entity.Client;

public interface IClientDao {

  public Iterable<Client> findAll();

  public Client save(Client client);

  public Client update(Client client);

  public Optional<Client> findById(Long id);

  public void deleteById(Long id);

}
