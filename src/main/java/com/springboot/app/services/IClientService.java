package com.springboot.app.services;

import java.util.List;
import java.util.Optional;

import com.springboot.app.models.entity.Client;

public interface IClientService {

  public List<Client> findAll();

  public void save(Client client);

  public Optional<Client> findById(Long id);

  public void delete(Long id);
}
