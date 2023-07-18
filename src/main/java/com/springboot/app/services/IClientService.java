package com.springboot.app.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Client;

public interface IClientService {

  Iterable<Client> findAll();

  Page<Client> findAll(Pageable pageable);

  Client saveOrUpdate(Client client);

  Optional<Client> findById(Long id);

  void delete(Long id);
}
