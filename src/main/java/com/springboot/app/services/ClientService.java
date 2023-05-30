package com.springboot.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IClientDaoCrudRepository;
import com.springboot.app.models.entity.Client;

@Primary
@Service("ClientService")
public class ClientService implements IClientService {

  @Autowired
  private IClientDaoCrudRepository clientDao;

  @Override
  @Transactional(readOnly = true)
  public Iterable<Client> findAll() {
    return clientDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Client> findById(Long id) {
    return clientDao.findById(id);
  }

  @Override
  @Transactional
  public void saveOrUpdate(Client client) {
    clientDao.save(client);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    clientDao.deleteById(id);

  }

}
