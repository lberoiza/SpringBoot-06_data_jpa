package com.springboot.app.services;

import java.util.Optional;

import com.springboot.app.models.entity.Client;

public interface IClientService {

  public Iterable<Client> findAll();

  public void saveOrUpdate(Client client);

  public Optional<Client> findById(Long id);

  public void delete(Long id);
}
