package com.springboot.app.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.entity.Client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Primary
@Repository("ClientDaoJpa")
public class ClientDaoJpa implements IClientDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  @Transactional(readOnly = true)
  public List<Client> findAll() {
    return em.createQuery("from Client", Client.class).getResultList();
  }

  @Override
  @Transactional
  public void save(Client client) {
    if (client.getId() != null) {
      em.merge(client);
    } else {
      em.persist(client);
    }

  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Client> findById(Long id) {
    return Optional.ofNullable(em.find(Client.class, id));
  }

}
