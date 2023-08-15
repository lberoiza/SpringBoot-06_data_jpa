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
public class ClientService extends BaseCRUDService<Client, IClientDaoRepository> {

  public Optional<Client> fetchClientByIdWithInvoices(Long id) {
    return Optional.of(this.repository.fetchClientByIdWithInvoices(id));
  }


}
