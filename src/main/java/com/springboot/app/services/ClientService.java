package com.springboot.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IClientDao;
import com.springboot.app.models.entity.Client;

@Primary
@Service("ClientService")
public class ClientService implements IClientService {

  @Autowired
  private IClientDao clientDao;

  @Override
  @Transactional(readOnly = true)
  public List<Client> findAll() {
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
    if (client.hasValidId()) {
      clientDao.update(client);
    } else {
      clientDao.save(client);
    }

  }

  @Override
  @Transactional
  public void delete(Long id) {
    clientDao.delete(id);

  }

}
