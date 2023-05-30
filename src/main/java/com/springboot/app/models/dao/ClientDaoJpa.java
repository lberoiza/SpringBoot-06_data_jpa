package com.springboot.app.models.dao;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.springboot.app.models.entity.Client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Primary
@Repository("ClientDaoJpa")
public class ClientDaoJpa implements IClientDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Iterable<Client> findAll() {
    return em.createQuery("from Client", Client.class).getResultList();
  }

  @Override
  public Optional<Client> findById(Long id) {
    return Optional.ofNullable(em.find(Client.class, id));
  }

  @Override
  public Client save(Client client) {
    em.persist(client);
    return client;
  }

  @Override
  public Client update(Client client) {
    if (!client.hasValidId()) {
      throw new IllegalStateException(String.format("The User has not a correrct Id: (%s)", client.getId()));
    }
    return em.merge(client);
  }

  @Override
  public void deleteById(Long id) {
    findById(id).ifPresent(client -> em.remove(client));
  }

}
