package com.springboot.app.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Client;

public interface IClientService {

  public Iterable<Client> findAll();

  public Page<Client> findAll(Pageable pageable);

  public void saveOrUpdate(Client client);

  public Optional<Client> findById(Long id);

  public void delete(Long id);
}
