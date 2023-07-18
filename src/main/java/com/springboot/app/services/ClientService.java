package com.springboot.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IClientDaoRepository;
import com.springboot.app.models.entity.Client;

@Primary
@Service("ClientService")
public class ClientService implements IClientService {

  @Autowired
  private IClientDaoRepository clientDao;


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
  public Optional<Client> fetchClientByIdWithInvoices(Long id) {
    return Optional.of(clientDao.fetchClientByIdWithInvoices(id));
  }

  @Override
  @Transactional
  public Client saveOrUpdate(Client client) {
    return clientDao.save(client);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    clientDao.deleteById(id);

  }

  @Override
  public Page<Client> findAll(Pageable pageable) {
    return clientDao.findAll(pageable);
  }

}
